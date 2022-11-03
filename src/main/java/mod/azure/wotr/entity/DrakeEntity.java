package mod.azure.wotr.entity;

import java.util.SplittableRandom;
import java.util.UUID;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import mod.azure.wotr.config.WoTRConfig;
import mod.azure.wotr.entity.ai.goals.DrakeFireAttackGoal;
import mod.azure.wotr.items.DrakeArmorItem;
import mod.azure.wotr.registry.WoTRSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class DrakeEntity extends WoTREntity implements Growable {

	public static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(DrakeEntity.class,
			EntityDataSerializers.FLOAT);
	private static final UUID HORSE_ARMOR_BONUS_ID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

	public DrakeEntity(EntityType<? extends AbstractHorse> entityType, Level world) {
		super(entityType, world);
		this.xpReward = WoTRConfig.drake_exp;
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 25.0D)
				.add(Attributes.MAX_HEALTH, WoTRConfig.drake_health)
				.add(Attributes.ATTACK_DAMAGE, WoTRConfig.drake_melee).add(Attributes.JUMP_STRENGTH, 3)
				.add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_KNOCKBACK, 0.0D);
	}

	@Override
	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if ((animationSpeedOld < 0.65F && !(animationSpeedOld > -0.10F && animationSpeedOld < 0.02F)) && this.onGround
				&& !this.wasTouchingWater && !this.hurtMarked) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		if (animationSpeedOld >= 0.65F && this.onGround && !this.wasEyeInWater && !this.hurtMarked) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("run", EDefaultLoopTypes.LOOP));
			event.getController().setAnimationSpeed(1 + this.zza);
			return PlayState.CONTINUE;
		}
		if (!this.onGround && this.wasEyeInWater && !(animationSpeedOld > -0.10F && animationSpeedOld < 0.01F)
				&& !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("swim_move", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		if (!this.onGround && this.wasTouchingWater && (animationSpeedOld > -0.10F && animationSpeedOld < 0.01F)
				&& !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("swim_idle", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		if (!this.onGround && !this.wasTouchingWater
				&& !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("jump", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		if ((this.entityData.get(STATE) == 2) && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("melee", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		if ((this.entityData.get(STATE) == 1) && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("fireball", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		if ((this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", EDefaultLoopTypes.PLAY_ONCE));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<DrakeEntity>(this, "idle_controller", 3, this::predicate));
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(7, new DrakeFireAttackGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return WoTRSounds.DRAKE_IDLE;
	}

	public int getVariant() {
		return Mth.clamp((Integer) this.entityData.get(VARIANT), 1, 3);
	}

	public int getVariants() {
		return 3;
	}

	public float getGrowth() {
		return entityData.get(GROWTH);
	}

	public void setGrowth(float growth) {
		entityData.set(GROWTH, growth);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(GROWTH, 0.0F);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putInt("Variant", this.getVariant());
		nbt.putFloat("growth", getGrowth());
		if (!this.inventory.getItem(1).isEmpty()) {
			nbt.put("ArmorItem", this.inventory.getItem(1).save(new CompoundTag()));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		ItemStack itemStack;
		super.readAdditionalSaveData(nbt);
		this.setVariant(nbt.getInt("Variant"));
		this.setGrowth(nbt.getFloat("growth"));
		if (nbt.contains("ArmorItem", Tag.TAG_COMPOUND)
				&& !(itemStack = ItemStack.of(nbt.getCompound("ArmorItem"))).isEmpty() && this.isArmor(itemStack)) {
			this.inventory.setItem(1, itemStack);
		}
		this.updateContainerEquipment();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!level.isClientSide() && this.isAlive()) {
			grow(this, (this.tickCount / 24000) * 1);
		}
	}

	@Override
	public float getMaxGrowth() {
		return 1200;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverWorldAccess, DifficultyInstance difficulty,
			MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag entityTag) {
		spawnData = super.finalizeSpawn(serverWorldAccess, difficulty, reason, spawnData, entityTag);
		SplittableRandom random = new SplittableRandom();
		int var = random.nextInt(0, 4);
		this.setVariant(var);
		if ((reason == MobSpawnType.CHUNK_GENERATION || reason == MobSpawnType.NATURAL)) {
			this.setTamed(false);
		} else {
			LivingEntity player = this.getCommandSenderWorld().getNearestPlayer(this, 15);
			if (player != null)
				this.tameWithName((Player) player);
			setGrowth(1200);
		}
		return spawnData;
	}

	@Override
	public boolean tameWithName(Player player) {
		this.setOwnerUUID(player.getUUID());
		this.setTamed(true);
		return true;
	}

	public ItemStack getArmorType() {
		return this.getItemBySlot(EquipmentSlot.CHEST);
	}

	private void equipArmor(ItemStack stack) {
		this.setItemSlot(EquipmentSlot.CHEST, stack);
		this.setDropChance(EquipmentSlot.CHEST, 0.0f);
	}

	@Override
	protected void updateContainerEquipment() {
		if (this.level.isClientSide()) {
			return;
		}
		super.updateContainerEquipment();
		this.setArmorTypeFromStack(this.inventory.getItem(1));
		this.setDropChance(EquipmentSlot.CHEST, 0.0f);
	}

	private void setArmorTypeFromStack(ItemStack stack) {
		this.equipArmor(stack);
		if (!this.level.isClientSide()) {
			int i;
			this.getAttribute(Attributes.ARMOR).removeModifier(HORSE_ARMOR_BONUS_ID);
			if (this.isArmor(stack) && (i = ((DrakeArmorItem) stack.getItem()).getBonus()) != 0) {
				this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(HORSE_ARMOR_BONUS_ID,
						"Horse armor bonus", (double) i, AttributeModifier.Operation.ADDITION));
			}
		}
	}

	@Override
	public void containerChanged(Container sender) {
		ItemStack itemStack = this.getArmorType();
		super.containerChanged(sender);
		ItemStack itemStack2 = this.getArmorType();
		if (this.age > 20 && this.isArmor(itemStack2) && itemStack != itemStack2) {
			this.playSound(SoundEvents.HORSE_ARMOR, 0.5f, 1.0f);
		}
	}

	@Override
	public boolean canWearArmor() {
		return true;
	}

	@Override
	public boolean isArmor(ItemStack item) {
		return item.getItem() instanceof DrakeArmorItem;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		Item item = itemStack.getItem();
		if (!this.isBaby()) {
			if (player.isSecondaryUseActive()) {
				this.openCustomInventoryScreen(player);
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}
			if (this.isVehicle()) {
				return super.mobInteract(player, hand);
			}
		}
		if (!itemStack.isEmpty()) {
			InteractionResult actionResult = itemStack.interactLivingEntity(player, this, hand);
			if (actionResult.consumesAction()) {
				return actionResult;
			}
			boolean bl = !this.isBaby() && !this.isSaddled() && itemStack.is(Items.SADDLE) && this.getGrowth() >= this.getMaxGrowth();
			if (this.isArmor(itemStack) || bl) {
				this.openCustomInventoryScreen(player);
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}
			if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth())
				this.heal(item.getFoodProperties().getNutrition());
		}
		if (this.getGrowth() < this.getMaxGrowth()) {
			return super.mobInteract(player, hand);
		}
		if (this.isBaby()) {
			return super.mobInteract(player, hand);
		}
		if (this.isSaddled())
			this.doPlayerRide(player);
		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		Item item = stack.getItem();
		return item.isEdible() && item.getFoodProperties().isMeat();
	}

	@Override
	public SlotAccess getSlot(int mappedIndex) {
		int j;
		int i = mappedIndex - 400;
		if (i >= 0 && i < 2 && i < this.inventory.getContainerSize()) {
			if (i == 0) {
				return this.createEquipmentSlotAccess(i, stack -> (stack.isEmpty() || stack.is(Items.SADDLE) && this.getGrowth() >= this.getMaxGrowth()));
			}
			if (i == 1) {
				if (!this.canWearArmor()) {
					return SlotAccess.NULL;
				}
				return this.createEquipmentSlotAccess(i, stack -> stack.isEmpty() || this.isArmor((ItemStack) stack));
			}
		}
		if ((j = mappedIndex - 500 + 2) >= 2 && j < this.inventory.getContainerSize()) {
			return SlotAccess.forContainer(this.inventory, j);
		}
		return super.getSlot(mappedIndex);
	}

	private SlotAccess createEquipmentSlotAccess(final int slot, final Predicate<ItemStack> predicate) {
		return new SlotAccess() {

			@Override
			public ItemStack get() {
				return DrakeEntity.this.inventory.getItem(slot);
			}

			@Override
			public boolean set(ItemStack stack) {
				if (!predicate.test(stack)) {
					return false;
				}
				DrakeEntity.this.inventory.setItem(slot, stack);
				DrakeEntity.this.updateContainerEquipment();
				return true;
			}
		};
	}

	@Override
	public void travel(Vec3 movementInput) {
		if (!this.isAlive()) {
			return;
		}
		LivingEntity livingEntity = this.getControllingPassenger();
		if (!this.isVehicle() || livingEntity == null) {
			this.flyingSpeed = 0.02f;
			super.travel(movementInput);
			return;
		}
		this.setYRot(livingEntity.getYRot());
		this.yRotO = this.getYRot();
		this.setXRot(livingEntity.getXRot() * 0.5f);
		this.setRot(this.getYRot(), this.getXRot());
		this.yHeadRot = this.yBodyRot = this.getYRot();
		float f = livingEntity.xxa * 0.5f;
		float g = livingEntity.zza;
		if (g <= 0.0f) {
			g *= 0.25f;
			this.gallopSoundCounter = 0;
		}
		if (this.playerJumpPendingScale > 0.0f && !this.isJumping() && this.onGround) {
			double d = this.getCustomJump() * (double) this.playerJumpPendingScale * (double) this.getBlockJumpFactor();
			double e = d + this.getJumpBoostPower();
			Vec3 vec3d = this.getDeltaMovement();
			this.setDeltaMovement(vec3d.x, e * this.playerJumpPendingScale, vec3d.z);
			this.setIsJumping(true);
			this.hasImpulse = true;
			if (g > 0.0f) {
				float h = Mth.sin(this.getYRot() * ((float) Math.PI / 180));
				float i = Mth.cos(this.getYRot() * ((float) Math.PI / 180));
				this.setDeltaMovement(this.getDeltaMovement().add(-3.8f * h * this.playerJumpPendingScale,
						1.0 * this.playerJumpPendingScale, 3.8f * i * this.playerJumpPendingScale));
			}
			this.playerJumpPendingScale = 0.0f;
		}
		this.flyingSpeed = this.getSpeed() * 0.1f;
		if (this.isControlledByLocalInstance()) {
			this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
			super.travel(new Vec3(f, movementInput.y, g));
		} else if (livingEntity instanceof Player) {
			this.setDeltaMovement(Vec3.ZERO);
		}
		if (this.onGround) {
			this.playerJumpPendingScale = 0.0f;
			this.setIsJumping(false);
		}
		this.calculateEntityAnimation(this, false);
		this.tryCheckInsideBlocks();
	}

	@Override
	public void openCustomInventoryScreen(Player player) {
		if (!this.level.isClientSide() && (!this.isVehicle() || this.hasPassenger(player))) {
			player.openHorseInventory(this, this.inventory);
		}
	}

	protected int getInventorySize() {
		return 2;
	}

	protected void onChestedStatusChanged() {
		SimpleContainer simpleInventory = this.inventory;
		this.inventory = new SimpleContainer(this.getInventorySize());
		if (simpleInventory != null) {
			simpleInventory.removeListener(this);
			int i = Math.min(simpleInventory.getContainerSize(), this.inventory.getContainerSize());
			for (int j = 0; j < i; ++j) {
				ItemStack itemStack = simpleInventory.getItem(j);
				if (itemStack.isEmpty())
					continue;
				this.inventory.setItem(j, itemStack.copy());
			}
		}
		this.inventory.addListener(this);
		this.updateContainerEquipment();
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		if (fallDistance > 1.0f) {
			this.playSound(SoundEvents.HORSE_LAND, 0.4f, 1.0f);
		}
		return false;
	}

	@Override
	public boolean isSaddleable() {
		return this.isAlive() && !this.isBaby();
	}

	@Override
	protected boolean isImmobile() {
		return super.isImmobile() && this.isVehicle() && this.isSaddled();
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	public void setSecondsOnFire(int seconds) {
		super.setSecondsOnFire(0);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		if (state.getMaterial().isLiquid()) {
			return;
		}
		BlockState blockState = this.level.getBlockState(pos.above());
		SoundType blockSoundGroup = state.getSoundType();
		if (blockState.is(Blocks.SNOW)) {
			blockSoundGroup = blockState.getSoundType();
		} else {
			++this.gallopSoundCounter;
			if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
				this.playSound(SoundEvents.RAVAGER_STEP, blockSoundGroup.getVolume() * 0.15f,
						blockSoundGroup.getPitch());
			} else if (this.gallopSoundCounter <= 5) {
				this.playSound(SoundEvents.RAVAGER_STEP, blockSoundGroup.getVolume() * 0.15f,
						blockSoundGroup.getPitch());
			}
		}
	}

	@Override
	protected void playJumpSound() {
		this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 0.4f, 1.0f);
	}

}

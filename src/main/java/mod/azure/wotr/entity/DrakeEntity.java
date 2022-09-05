package mod.azure.wotr.entity;

import java.util.SplittableRandom;
import java.util.UUID;
import java.util.function.Predicate;

import mod.azure.wotr.config.WoTRConfig;
import mod.azure.wotr.entity.ai.goals.AbstractRangedAttack;
import mod.azure.wotr.entity.ai.goals.AttackSound;
import mod.azure.wotr.entity.ai.goals.DrakeFireAttackGoal;
import mod.azure.wotr.entity.projectiles.mobs.DrakeFireProjectile;
import mod.azure.wotr.items.DrakeArmorItem;
import mod.azure.wotr.registry.WoTRSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class DrakeEntity extends WoTREntity {

	private static final UUID HORSE_ARMOR_BONUS_ID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

	public DrakeEntity(EntityType<? extends AbstractHorseEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = WoTRConfig.drake_exp;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25.0D)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, WoTRConfig.drake_health)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, WoTRConfig.drake_melee)
				.add(EntityAttributes.HORSE_JUMP_STRENGTH, 3).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.0D);
	}

	@Override
	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if ((lastLimbDistance < 0.65F && !(lastLimbDistance > -0.10F && lastLimbDistance < 0.02F)) && this.onGround
				&& !this.touchingWater && !this.velocityModified) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
			return PlayState.CONTINUE;
		}
		if (lastLimbDistance >= 0.65F && this.onGround && !this.submergedInWater && !this.velocityModified) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true));
			event.getController().setAnimationSpeed(1 + this.forwardSpeed);
			return PlayState.CONTINUE;
		}
		if (!this.onGround && this.submergedInWater && !(lastLimbDistance > -0.10F && lastLimbDistance < 0.01F)
				&& !(this.dead || this.getHealth() < 0.01 || this.isDead())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("swim_move", true));
			return PlayState.CONTINUE;
		}
		if (!this.onGround && this.touchingWater && (lastLimbDistance > -0.10F && lastLimbDistance < 0.01F)
				&& !(this.dead || this.getHealth() < 0.01 || this.isDead())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("swim_idle", true));
			return PlayState.CONTINUE;
		}
		if (!this.onGround && !this.touchingWater && !(this.dead || this.getHealth() < 0.01 || this.isDead())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("jump", true));
			return PlayState.CONTINUE;
		}
		if ((this.dataTracker.get(STATE) == 2) && !(this.dead || this.getHealth() < 0.01 || this.isDead())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("melee", true));
			return PlayState.CONTINUE;
		}
		if ((this.dataTracker.get(STATE) == 1) && !(this.dead || this.getHealth() < 0.01 || this.isDead())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("fireball", true));
			return PlayState.CONTINUE;
		}
		if ((this.dead || this.getHealth() < 0.01 || this.isDead())) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<DrakeEntity>(this, "idle_controller", 3, this::predicate));
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(4, new DrakeFireAttackGoal(this,
				new FireballAttack(this).setProjectileOriginOffset(0.8, 0.8, 0.8).setDamage(WoTRConfig.drake_ranged)));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, HostileEntity.class, true));
	}

	public class FireballAttack extends AbstractRangedAttack {

		public FireballAttack(DrakeEntity parentEntity, double xOffSetModifier, double entityHeightFraction,
				double zOffSetModifier, float damage) {
			super(parentEntity, xOffSetModifier, entityHeightFraction, zOffSetModifier, damage);
		}

		public FireballAttack(DrakeEntity parentEntity) {
			super(parentEntity);
		}

		@Override
		public AttackSound getDefaultAttackSound() {
			return new AttackSound(SoundEvents.BLOCK_CAMPFIRE_CRACKLE, 1, 1);
		}

		@Override
		public ProjectileEntity getProjectile(World world, double d2, double d3, double d4) {
			return new DrakeFireProjectile(world, this.parentEntity, d2, d3, d4);

		}
	}

	@Override
	protected void initCustomGoals() {
	}

	@Override
	public int getArmor() {
		return WoTRConfig.drake_armor;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return WoTRSounds.DRAKE_IDLE;
	}

	public int getVariant() {
		return MathHelper.clamp((Integer) this.dataTracker.get(VARIANT), 1, 3);
	}

	public int getVariants() {
		return 3;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("Variant", this.getVariant());
		if (!this.items.getStack(1).isEmpty()) {
			nbt.put("ArmorItem", this.items.getStack(1).writeNbt(new NbtCompound()));
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		ItemStack itemStack;
		super.readCustomDataFromNbt(nbt);
		this.setVariant(nbt.getInt("Variant"));
		if (nbt.contains("ArmorItem", NbtElement.COMPOUND_TYPE)
				&& !(itemStack = ItemStack.fromNbt(nbt.getCompound("ArmorItem"))).isEmpty()
				&& this.isHorseArmor(itemStack)) {
			this.items.setStack(1, itemStack);
		}
		this.updateSaddle();
	}

	@Override
	public EntityData initialize(ServerWorldAccess serverWorldAccess, LocalDifficulty difficulty,
			SpawnReason spawnReason, EntityData entityData, NbtCompound entityTag) {
		entityData = super.initialize(serverWorldAccess, difficulty, spawnReason, entityData, entityTag);
		SplittableRandom random = new SplittableRandom();
		int var = random.nextInt(0, 4);
		this.setVariant(var);
		if ((spawnReason == SpawnReason.CHUNK_GENERATION || spawnReason == SpawnReason.NATURAL)) {
			this.setTame(false);
		} else {
			LivingEntity player = this.getEntityWorld().getClosestPlayer(this, 15);
			if (player != null)
				this.bondWithPlayer((PlayerEntity) player);
		}
		return entityData;
	}

	@Override
	public boolean bondWithPlayer(PlayerEntity player) {
		this.setOwnerUuid(player.getUuid());
		this.setTame(true);
		return true;
	}

	public ItemStack getArmorType() {
		return this.getEquippedStack(EquipmentSlot.CHEST);
	}

	private void equipArmor(ItemStack stack) {
		this.equipStack(EquipmentSlot.CHEST, stack);
		this.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0f);
	}

	@Override
	protected void updateSaddle() {
		if (this.world.isClient) {
			return;
		}
		super.updateSaddle();
		this.setArmorTypeFromStack(this.items.getStack(1));
		this.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0f);
	}

	private void setArmorTypeFromStack(ItemStack stack) {
		this.equipArmor(stack);
		if (!this.world.isClient) {
			int i;
			this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).removeModifier(HORSE_ARMOR_BONUS_ID);
			if (this.isHorseArmor(stack) && (i = ((DrakeArmorItem) stack.getItem()).getBonus()) != 0) {
				this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR)
						.addTemporaryModifier(new EntityAttributeModifier(HORSE_ARMOR_BONUS_ID, "Horse armor bonus",
								(double) i, EntityAttributeModifier.Operation.ADDITION));
			}
		}
	}

	@Override
	public void onInventoryChanged(Inventory sender) {
		ItemStack itemStack = this.getArmorType();
		super.onInventoryChanged(sender);
		ItemStack itemStack2 = this.getArmorType();
		if (this.age > 20 && this.isHorseArmor(itemStack2) && itemStack != itemStack2) {
			this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5f, 1.0f);
		}
	}

	@Override
	public boolean hasArmorSlot() {
		return true;
	}

	@Override
	public boolean isHorseArmor(ItemStack item) {
		return item.getItem() instanceof DrakeArmorItem;
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		Item item = itemStack.getItem();
		if (!this.isBaby()) {
			if (player.shouldCancelInteraction()) {
				this.openInventory(player);
				return ActionResult.success(this.world.isClient);
			}
			if (this.hasPassengers()) {
				return super.interactMob(player, hand);
			}
		}
		if (!itemStack.isEmpty()) {
			ActionResult actionResult = itemStack.useOnEntity(player, this, hand);
			if (actionResult.isAccepted()) {
				return actionResult;
			}
			boolean bl = !this.isBaby() && !this.isSaddled() && itemStack.isOf(Items.SADDLE);
			if (this.isHorseArmor(itemStack) || bl) {
				this.openInventory(player);
				return ActionResult.success(this.world.isClient);
			}
			if (this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth())
				this.heal(item.getFoodComponent().getHunger());
		}
		if (this.isBaby()) {
			return super.interactMob(player, hand);
		}
		if (this.isSaddled())
			this.putPlayerOnBack(player);
		return ActionResult.success(this.world.isClient);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		Item item = stack.getItem();
		return item.isFood() && item.getFoodComponent().isMeat();
	}

	@Override
	public StackReference getStackReference(int mappedIndex) {
		int j;
		int i = mappedIndex - 400;
		if (i >= 0 && i < 2 && i < this.items.size()) {
			if (i == 0) {
				return this.createInventoryStackReference(i, stack -> stack.isEmpty() || stack.isOf(Items.SADDLE));
			}
			if (i == 1) {
				if (!this.hasArmorSlot()) {
					return StackReference.EMPTY;
				}
				return this.createInventoryStackReference(i,
						stack -> stack.isEmpty() || this.isHorseArmor((ItemStack) stack));
			}
		}
		if ((j = mappedIndex - 500 + 2) >= 2 && j < this.items.size()) {
			return StackReference.of(this.items, j);
		}
		return super.getStackReference(mappedIndex);
	}

	private StackReference createInventoryStackReference(final int slot, final Predicate<ItemStack> predicate) {
		return new StackReference() {

			@Override
			public ItemStack get() {
				return DrakeEntity.this.items.getStack(slot);
			}

			@Override
			public boolean set(ItemStack stack) {
				if (!predicate.test(stack)) {
					return false;
				}
				DrakeEntity.this.items.setStack(slot, stack);
				DrakeEntity.this.updateSaddle();
				return true;
			}
		};
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (!this.isAlive()) {
			return;
		}
		LivingEntity livingEntity = this.getPrimaryPassenger();
		if (!this.hasPassengers() || livingEntity == null) {
			this.airStrafingSpeed = 0.02f;
			super.travel(movementInput);
			return;
		}
		this.setYaw(livingEntity.getYaw());
		this.prevYaw = this.getYaw();
		this.setPitch(livingEntity.getPitch() * 0.5f);
		this.setRotation(this.getYaw(), this.getPitch());
		this.headYaw = this.bodyYaw = this.getYaw();
		float f = livingEntity.sidewaysSpeed * 0.5f;
		float g = livingEntity.forwardSpeed;
		if (g <= 0.0f) {
			g *= 0.25f;
			this.soundTicks = 0;
		}
		if (this.jumpStrength > 0.0f && !this.isInAir() && this.onGround) {
			double d = this.getJumpStrength() * (double) this.jumpStrength * (double) this.getJumpVelocityMultiplier();
			double e = d + this.getJumpBoostVelocityModifier();
			Vec3d vec3d = this.getVelocity();
			this.setVelocity(vec3d.x, e * this.jumpStrength, vec3d.z);
			this.setInAir(true);
			this.velocityDirty = true;
			if (g > 0.0f) {
				float h = MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
				float i = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
				this.setVelocity(this.getVelocity().add(-3.8f * h * this.jumpStrength, 1.0 * this.jumpStrength,
						3.8f * i * this.jumpStrength));
			}
			this.jumpStrength = 0.0f;
		}
		this.airStrafingSpeed = this.getMovementSpeed() * 0.1f;
		if (this.isLogicalSideForUpdatingMovement()) {
			this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
			super.travel(new Vec3d(f, movementInput.y, g));
		} else if (livingEntity instanceof PlayerEntity) {
			this.setVelocity(Vec3d.ZERO);
		}
		if (this.onGround) {
			this.jumpStrength = 0.0f;
			this.setInAir(false);
		}
		this.updateLimbs(this, false);
		this.tryCheckBlockCollision();
	}

	@Override
	public void openInventory(PlayerEntity player) {
		if (!this.world.isClient && (!this.hasPassengers() || this.hasPassenger(player))) {
			player.openHorseInventory(this, this.items);
		}
	}

	protected int getInventorySize() {
		return 2;
	}

	protected void onChestedStatusChanged() {
		SimpleInventory simpleInventory = this.items;
		this.items = new SimpleInventory(this.getInventorySize());
		if (simpleInventory != null) {
			simpleInventory.removeListener(this);
			int i = Math.min(simpleInventory.size(), this.items.size());
			for (int j = 0; j < i; ++j) {
				ItemStack itemStack = simpleInventory.getStack(j);
				if (itemStack.isEmpty())
					continue;
				this.items.setStack(j, itemStack.copy());
			}
		}
		this.items.addListener(this);
		this.updateSaddle();
	}

	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		if (fallDistance > 1.0f) {
			this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4f, 1.0f);
		}
		return false;
	}

	@Override
	public boolean canBeSaddled() {
		return this.isAlive() && !this.isBaby();
	}

	@Override
	protected boolean isImmobile() {
		return super.isImmobile() && this.hasPassengers() && this.isSaddled();
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	public void setOnFireFor(int seconds) {
		super.setOnFireFor(0);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		if (state.getMaterial().isLiquid()) {
			return;
		}
		BlockState blockState = this.world.getBlockState(pos.up());
		BlockSoundGroup blockSoundGroup = state.getSoundGroup();
		if (blockState.isOf(Blocks.SNOW)) {
			blockSoundGroup = blockState.getSoundGroup();
		} else {
			++this.soundTicks;
			if (this.soundTicks > 5 && this.soundTicks % 3 == 0) {
				this.playSound(SoundEvents.ENTITY_RAVAGER_STEP, blockSoundGroup.getVolume() * 0.15f,
						blockSoundGroup.getPitch());
			} else if (this.soundTicks <= 5) {
				this.playSound(SoundEvents.ENTITY_RAVAGER_STEP, blockSoundGroup.getVolume() * 0.15f,
						blockSoundGroup.getPitch());
			}
		}
	}

	@Override
	protected void playJumpSound() {
		this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_FLAP, 0.4f, 1.0f);
	}

}

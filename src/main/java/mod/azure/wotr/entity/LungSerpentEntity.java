package mod.azure.wotr.entity;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.registry.WoTRSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LungSerpentEntity extends WoTREntity implements Growable {

	public static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(LungSerpentEntity.class, EntityDataSerializers.FLOAT);

	public LungSerpentEntity(EntityType<? extends AbstractHorse> entityType, Level world) {
		super(entityType, world);
		this.xpReward = WoTRMod.config.lung_serpent_exp;
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 25.0D).add(Attributes.MAX_HEALTH, WoTRMod.config.lung_serpent_health).add(Attributes.ATTACK_DAMAGE, 0).add(Attributes.JUMP_STRENGTH, 0).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_KNOCKBACK, 0.0D);
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "idle_controller", 10, event -> {
			if (event.isMoving() && !this.hurtMarked)
				return event.setAndContinue(RawAnimation.begin().thenLoop("moving"));
			if ((this.entityData.get(STATE) == 1) && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying()))
				return event.setAndContinue(RawAnimation.begin().thenPlayXTimes("repel", 1));
			if ((this.dead || this.getHealth() < 0.01 || this.isDeadOrDying()))
				return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("death"));
			return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
		}));
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return WoTRSounds.DRAKE_IDLE;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
    }

	public int getVariant() {
		return Mth.clamp(this.entityData.get(VARIANT), 1, 4);
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
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		this.setVariant(nbt.getInt("Variant"));
		this.setGrowth(nbt.getFloat("growth"));
		this.updateContainerEquipment();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!level().isClientSide() && this.isAlive())
			grow(this, (this.tickCount / 24000) * 1);
	}

	@Override
	public float getMaxGrowth() {
		return 1200;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverWorldAccess, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag entityTag) {
		spawnData = super.finalizeSpawn(serverWorldAccess, difficulty, reason, spawnData, entityTag);
		var var = this.getRandom().nextInt(0, 4);
		this.setVariant(var);
		if ((reason == MobSpawnType.CHUNK_GENERATION || reason == MobSpawnType.NATURAL)) {
			this.setTamed(false);
		} else {
			var player = this.getCommandSenderWorld().getNearestPlayer(this, 15);
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

	@Override
	public boolean canWearArmor() {
		return false;
	}

	@Override
	public boolean isArmor(ItemStack item) {
		return false;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		var itemStack = player.getItemInHand(hand);
		var item = itemStack.getItem();
		if (!this.isBaby()) {
			if (player.isSecondaryUseActive()) {
				this.openCustomInventoryScreen(player);
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}
			if (this.isVehicle())
				return super.mobInteract(player, hand);
		}
		if (!itemStack.isEmpty()) {
			var actionResult = itemStack.interactLivingEntity(player, this, hand);
			if (actionResult.consumesAction())
				return actionResult;
			var bl = !this.isBaby() && !this.isSaddled() && itemStack.is(Items.SADDLE) && this.getGrowth() >= this.getMaxGrowth();
			if (bl) {
				this.openCustomInventoryScreen(player);
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}
			if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth())
				this.heal(item.getFoodProperties().getNutrition());
		}
		if (this.getGrowth() < this.getMaxGrowth())
			return super.mobInteract(player, hand);
		if (this.isBaby())
			return super.mobInteract(player, hand);
		if (this.isSaddled())
			this.doPlayerRide(player);
		return InteractionResult.sidedSuccess(this.level().isClientSide);
	}

	@Override
	protected void executeRidersJump(float f, Vec3 vec3) {
		super.executeRidersJump(f, vec3);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		var item = stack.getItem();
		return item.isEdible() && item.getFoodProperties().isMeat();
	}

	@Override
	public SlotAccess getSlot(int mappedIndex) {
		int j;
		var i = mappedIndex - 400;
		if (i >= 0 && i < 2 && i < this.inventory.getContainerSize()) {
			if (i == 0)
				return this.createEquipmentSlotAccess(i, stack -> (stack.isEmpty() || stack.is(Items.SADDLE) && this.getGrowth() >= this.getMaxGrowth()));
			if (i == 1) {
				if (!this.canWearArmor())
					return SlotAccess.NULL;
				return this.createEquipmentSlotAccess(i, stack -> stack.isEmpty() || this.isArmor((ItemStack) stack));
			}
		}
		if ((j = mappedIndex - 500 + 2) >= 2 && j < this.inventory.getContainerSize())
			return SlotAccess.forContainer(this.inventory, j);
		return super.getSlot(mappedIndex);
	}

	private SlotAccess createEquipmentSlotAccess(final int slot, final Predicate<ItemStack> predicate) {
		return new SlotAccess() {

			@Override
			public ItemStack get() {
				return LungSerpentEntity.this.inventory.getItem(slot);
			}

			@Override
			public boolean set(ItemStack stack) {
				if (!predicate.test(stack))
					return false;
				LungSerpentEntity.this.inventory.setItem(slot, stack);
				LungSerpentEntity.this.updateContainerEquipment();
				return true;
			}
		};
	}

	@Override
	public void travel(Vec3 movementInput) {
		if (!this.isAlive())
			return;
		var livingEntity = this.getControllingPassenger();
		if (!this.isVehicle() || livingEntity == null) {
			super.travel(movementInput);
			return;
		}
		this.setYRot(livingEntity.getYRot());
		this.yRotO = this.getYRot();
		this.setXRot(livingEntity.getXRot() * 0.5f);
		this.setRot(this.getYRot(), this.getXRot());
		this.yHeadRot = this.yBodyRot = this.getYRot();
		var f = livingEntity.xxa * 0.5f;
		var g = livingEntity.zza;
		if (g <= 0.0f) {
			g *= 0.25f;
			this.gallopSoundCounter = 0;
		}
		if (this.playerJumpPendingScale > 0.0f && !this.isJumping() && this.onGround()) {
			var d = this.getCustomJump() * (double) this.playerJumpPendingScale * (double) this.getBlockJumpFactor();
			var e = d + this.getJumpBoostPower();
			var vec3d = this.getDeltaMovement();
			this.setDeltaMovement(vec3d.x, e * this.playerJumpPendingScale, vec3d.z);
			this.setIsJumping(true);
			this.hasImpulse = true;
			if (g > 0.0f) {
				float h = Mth.sin(this.getYRot() * ((float) Math.PI / 180));
				float i = Mth.cos(this.getYRot() * ((float) Math.PI / 180));
				this.setDeltaMovement(this.getDeltaMovement().add(-3.8f * h * this.playerJumpPendingScale, 1.0 * this.playerJumpPendingScale, 3.8f * i * this.playerJumpPendingScale));
			}
			this.playerJumpPendingScale = 0.0f;
		}
		if (this.isControlledByLocalInstance()) {
			this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
			super.travel(new Vec3(f, movementInput.y, g));
		} else if (livingEntity instanceof Player)
			this.setDeltaMovement(Vec3.ZERO);
		this.playerJumpPendingScale = 0.0f;
		this.setIsJumping(false);
		this.tryCheckInsideBlocks();
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	public boolean onClimbable() {
		return false;
	}

	@Override
	public void openCustomInventoryScreen(Player player) {
		if (!this.level().isClientSide() && (!this.isVehicle() || this.hasPassenger(player)))
			player.openHorseInventory(this, this.inventory);
	}

	protected int getInventorySize() {
		return 2;
	}

	protected void onChestedStatusChanged() {
		var simpleInventory = this.inventory;
		this.inventory = new SimpleContainer(this.getInventorySize());
		if (simpleInventory != null) {
			simpleInventory.removeListener(this);
			var i = Math.min(simpleInventory.getContainerSize(), this.inventory.getContainerSize());
			for (var j = 0; j < i; ++j) {
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
		return false;
	}

	@Override
	public boolean isSaddleable() {
		return this.isAlive() && !this.isBaby();
	}

	@Override
	public boolean isImmobile() {
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

}

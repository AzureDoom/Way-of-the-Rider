package mod.azure.wotr.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class WoTREntity extends AbstractHorse implements GeoEntity {

	public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(WoTREntity.class,
			EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(WoTREntity.class,
			EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(WoTREntity.class,
			EntityDataSerializers.BOOLEAN);
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public WoTREntity(EntityType<? extends AbstractHorse> entityType, Level Level) {
		super(entityType, Level);
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, event -> {
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F, 0.02f, true));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
	}

	public int getAttckingState() {
		return this.entityData.get(STATE);
	}

	public void setAttackingState(int time) {
		this.entityData.set(STATE, time);
	}

	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_IS_CHARGING);
	}

	public void setCharging(boolean charging) {
		this.entityData.set(DATA_IS_CHARGING, charging);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(STATE, 0);
		this.entityData.define(VARIANT, 0);
		this.entityData.define(DATA_IS_CHARGING, false);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putInt("getAttckingState", getAttckingState());
		nbt.putBoolean("isCharging", isCharging());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		setAttackingState(nbt.getInt("getAttckingState"));
		setCharging(nbt.getBoolean("isCharging"));
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 25) {
			this.remove(Entity.RemovalReason.KILLED);
		}
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public float getSoundVolume() {
		return 0.5F;
	}

	@Override
	public void playAmbientSound() {
		SoundEvent soundEvent = this.getAmbientSound();
		if (soundEvent != null) {
			this.playSound(soundEvent, 0.25F, this.getVoicePitch());
		}
	}

	public static boolean canSpawn(EntityType<WoTREntity> type, LevelAccessor Level, MobSpawnType reason, BlockPos pos,
			RandomSource random) {
		if (Level.getDifficulty() == Difficulty.PEACEFUL)
			return false;
		if ((reason != MobSpawnType.CHUNK_GENERATION && reason != MobSpawnType.NATURAL))
			return !Level.getBlockState(pos.below()).is(BlockTags.LOGS)
					&& !Level.getBlockState(pos.below()).is(BlockTags.LEAVES);
		return !Level.getBlockState(pos.below()).is(BlockTags.LOGS)
				&& !Level.getBlockState(pos.below()).is(BlockTags.LEAVES);
	}

}

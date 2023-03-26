package mod.azure.wotr.entity;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.wotr.entity.projectiles.mobs.DrakeFireProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

public abstract class WoTREntity extends AbstractHorse implements GeoEntity, SmartBrainOwner<WoTREntity> {

	public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(WoTREntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(WoTREntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(WoTREntity.class, EntityDataSerializers.BOOLEAN);
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

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
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	protected void customServerAiStep() {
		tickBrain(this);
	}

	@Override
	public List<ExtendedSensor<WoTREntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(), new NearbyLivingEntitySensor<WoTREntity>().setPredicate((target, entity) -> target instanceof Monster), new HurtBySensor<>(), new UnreachableTargetSensor<WoTREntity>());
	}

	@Override
	public BrainActivityGroup<WoTREntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new MoveToWalkTarget<>(), new FloatToSurfaceOfFluid<>());
	}

	@Override
	public BrainActivityGroup<WoTREntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(new FirstApplicableBehaviour<WoTREntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().stopIf(target -> !target.isAlive() || target instanceof Player && ((Player) target).isCreative()), new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(1).startCondition(entity -> !entity.isAggressive()), new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<WoTREntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().stopIf(target -> !target.isAlive() || target instanceof Player && ((Player) target).isCreative()), new SetWalkTargetToAttackTarget<>().speedMod(0.5F));
	}

	@Override
	protected void registerGoals() {
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
		var soundEvent = this.getAmbientSound();
		if (soundEvent != null)
			this.playSound(soundEvent, 0.25F, this.getVoicePitch());
	}

	public static boolean canSpawn(EntityType<WoTREntity> type, LevelAccessor Level, MobSpawnType reason, BlockPos pos, RandomSource random) {
		if (Level.getDifficulty() == Difficulty.PEACEFUL)
			return false;
		if ((reason != MobSpawnType.CHUNK_GENERATION && reason != MobSpawnType.NATURAL))
			return !Level.getBlockState(pos.below()).is(BlockTags.LOGS) && !Level.getBlockState(pos.below()).is(BlockTags.LEAVES);
		return !Level.getBlockState(pos.below()).is(BlockTags.LOGS) && !Level.getBlockState(pos.below()).is(BlockTags.LEAVES);
	}

	public void shootFlames(Entity target) {
		if (!this.level.isClientSide) {
			if (this.getTarget() != null) {
				var livingentity = this.getTarget();
				var world = this.getCommandSenderWorld();
				var vector3d = this.getViewVector(1.0F);
				var x = livingentity.getX() - (this.getX() + vector3d.x * 2);
				var y = livingentity.getY(0.5) - (this.getY(0.5));
				var z = livingentity.getZ() - (this.getZ() + vector3d.z * 2);
				var projectile = new DrakeFireProjectile(level, this, x, y, z);
				projectile.setPos(this.getX() + vector3d.x * 2, this.getY(0.5), this.getZ() + vector3d.z * 2);
				world.addFreshEntity(projectile);
			}
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return source == damageSources().inWall() || source == damageSources().inFire() || source == damageSources().onFire() ? false : super.hurt(source, amount);
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 30) {
			this.remove(RemovalReason.KILLED);
			this.dropExperience();
		}
	}

}

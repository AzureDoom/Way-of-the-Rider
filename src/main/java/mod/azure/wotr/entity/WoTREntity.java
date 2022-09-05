package mod.azure.wotr.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class WoTREntity extends AbstractHorseEntity implements IAnimatable, IAnimationTickable {

	public static final TrackedData<Integer> STATE = DataTracker.registerData(WoTREntity.class,
			TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Integer> VARIANT = DataTracker.registerData(WoTREntity.class,
			TrackedDataHandlerRegistry.INTEGER);
	public AnimationFactory factory = new AnimationFactory(this);

	public WoTREntity(EntityType<? extends AbstractHorseEntity> entityType, World world) {
		super(entityType, world);
		this.ignoreCameraFrustum = true;
	}

	public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<WoTREntity>(this, "idle_controller", 3, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(8, new LookAtEntityGoal(this, LivingEntity.class, 8.0F, 0.02f, true));
		this.goalSelector.add(8, new LookAroundGoal(this));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8D));
	}

	public int getAttckingState() {
		return this.dataTracker.get(STATE);
	}

	public void setAttackingState(int time) {
		this.dataTracker.set(STATE, time);
	}

	public void setVariant(int variant) {
		this.dataTracker.set(VARIANT, variant);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(STATE, 0);
		this.dataTracker.startTracking(VARIANT, 0);
	}

	@Override
	protected void updatePostDeath() {
		++this.deathTime;
		if (this.deathTime == 25) {
			this.remove(Entity.RemovalReason.KILLED);
		}
	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}

	@Override
	public EntityGroup getGroup() {
		return EntityGroup.DEFAULT;
	}

	@Override
	public float getSoundVolume() {
		return 0.5F;
	}

	@Override
	public void playAmbientSound() {
		SoundEvent soundEvent = this.getAmbientSound();
		if (soundEvent != null) {
			this.playSound(soundEvent, 0.25F, this.getSoundPitch());
		}
	}

	@Override
	public int tickTimer() {
		return age;
	}

	public static boolean canSpawn(EntityType<WoTREntity> type, WorldAccess world, SpawnReason reason, BlockPos pos,
			Random random) {
		if (world.getDifficulty() == Difficulty.PEACEFUL)
			return false;
		if ((reason != SpawnReason.CHUNK_GENERATION && reason != SpawnReason.NATURAL))
			return !world.getBlockState(pos.down()).isIn(BlockTags.LOGS)
					&& !world.getBlockState(pos.down()).isIn(BlockTags.LEAVES);
		return !world.getBlockState(pos.down()).isIn(BlockTags.LOGS)
				&& !world.getBlockState(pos.down()).isIn(BlockTags.LEAVES);
	}

}

package mod.azure.wotr.entity.ai.goals;

import java.util.EnumSet;

import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.sound.SoundEvents;

public class DrakeFireAttackGoal extends Goal {
	private final DrakeEntity entity;
	private int attackTime = -1;
	private AbstractRangedAttack attack;

	public DrakeFireAttackGoal(DrakeEntity mob, AbstractRangedAttack attack) {
		this.entity = mob;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
		this.attack = attack;
	}

	@Override
	public boolean canStart() {
		return this.entity.getTarget() != null;
	}

	@Override
	public boolean shouldContinue() {
		return this.canStart();
	}

	@Override
	public void start() {
		super.start();
		this.attackTime = 0;
		this.entity.setAttacking(true);
		this.entity.getNavigation().stop();
	}

	@Override
	public void stop() {
		super.stop();
		this.entity.setAttacking(false);
		this.entity.setAttackingState(0);
		this.attackTime = -1;
	}

	@Override
	public void tick() {
		LivingEntity livingentity = this.entity.getTarget();
		if (livingentity != null) {
			boolean inLineOfSight = this.entity.getVisibilityCache().canSee(livingentity);
			this.attackTime++;
			this.entity.lookAtEntity(livingentity, 30.0F, 30.0F);
			if (inLineOfSight) {
				if (this.entity.distanceTo(livingentity) >= 3.0D) {
					this.entity.getNavigation().stop();
					this.entity.getLookControl().lookAt(livingentity.getX(), livingentity.getEyeY(),
							livingentity.getZ());
					if (this.attackTime == 1) {
						this.entity.setAttackingState(0);
					}
					if (this.attackTime == 6) {
						this.entity.setAttackingState(1);
					}
					if (this.attackTime >= 12) {
						entity.playSound(SoundEvents.BLOCK_FIRE_AMBIENT, 1.0F, 1.0F);
						this.attack.shoot();
					}
					if (this.attackTime == 20) {
						this.attackTime = 0;
						this.entity.setAttackingState(0);
						this.entity.getNavigation().startMovingTo(livingentity, 1.0);
					}
				} else {
					this.entity.getLookControl().lookAt(livingentity.getX(), livingentity.getEyeY(),
							livingentity.getZ());
					if (this.attackTime == 1) {
						this.entity.setAttackingState(2);
					}
					if (this.attackTime == 3) {
						this.entity.tryAttack(livingentity);
					}
					if (this.attackTime == 20) {
						this.attackTime = 0;
						this.entity.setAttackingState(0);
						this.entity.getNavigation().startMovingTo(livingentity, 1.0);
					}
				}
			}
		}
	}
}

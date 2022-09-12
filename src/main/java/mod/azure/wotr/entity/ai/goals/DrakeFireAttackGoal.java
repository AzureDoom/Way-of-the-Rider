package mod.azure.wotr.entity.ai.goals;

import mod.azure.wotr.entity.DrakeEntity;
import mod.azure.wotr.entity.projectiles.mobs.DrakeFireProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class DrakeFireAttackGoal extends Goal {
	private final DrakeEntity ghast;
	public int chargeTime;

	public DrakeFireAttackGoal(DrakeEntity ghast) {
		this.ghast = ghast;
	}

	@Override
	public boolean canUse() {
		return this.ghast.getTarget() != null;
	}

	@Override
	public void start() {
		this.chargeTime = 0;
	}

	@Override
	public void stop() {
		this.ghast.setAttackingState(0);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		LivingEntity livingEntity = this.ghast.getTarget();
		if (livingEntity == null) {
			return;
		}
		boolean inLineOfSight = this.ghast.getSensing().hasLineOfSight(livingEntity);
		this.chargeTime++;
		this.ghast.lookAt(livingEntity, 30.0F, 30.0F);
		if (inLineOfSight) {
			if (this.ghast.distanceTo(livingEntity) >= 3.0D && this.ghast.getAttckingState() != 2) {
				this.ghast.getNavigation().stop();
				this.ghast.getLookControl().setLookAt(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
				if (this.chargeTime == 1) {
					this.ghast.setAttackingState(0);
				}
				if (this.chargeTime == 6) {
					this.ghast.setAttackingState(1);
				}
				if (this.chargeTime == 12) {
					Vec3 vec3 = this.ghast.getViewVector(1.0f);
					double f = livingEntity.getX() - (this.ghast.getX() + vec3.x * 4.0);
					double g = livingEntity.getY(0.5) - (0.5 + this.ghast.getY(0.5));
					double h = livingEntity.getZ() - (this.ghast.getZ() + vec3.z * 4.0);
					if (!this.ghast.isSilent()) {
						this.ghast.level.playSound(null, ghast, SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 1.0F,
								1.0F);
					}
					DrakeFireProjectile largeFireball = new DrakeFireProjectile(this.ghast.level,
							(LivingEntity) this.ghast, f, g, h);
					largeFireball.setPos(this.ghast.getX() + vec3.x * 4.0, this.ghast.getY(0.5) + 0.5,
							largeFireball.getZ() + vec3.z * 4.0);
					this.ghast.level.addFreshEntity(largeFireball);
				}
				if (this.chargeTime > 20) {
					this.chargeTime = 0;
					this.ghast.setAttackingState(0);
					this.ghast.getNavigation().moveTo(livingEntity, 1.0);
				}
			} else if (this.ghast.getAttckingState() != 1) {
				this.ghast.getLookControl().setLookAt(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
				if (this.chargeTime == 1) {
					this.ghast.setAttackingState(2);
				}
				if (this.chargeTime == 3) {
					this.ghast.doHurtTarget(livingEntity);
				}
				if (this.chargeTime > 20) {
					this.chargeTime = 0;
					this.ghast.setAttackingState(0);
					this.ghast.getNavigation().moveTo(livingEntity, 1.0);
				}
			}
		} else if (this.chargeTime > 0) {
			--this.chargeTime;
		}
		this.ghast.setCharging(this.chargeTime > 10);
	}
}

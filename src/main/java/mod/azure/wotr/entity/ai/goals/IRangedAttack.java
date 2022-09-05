
package mod.azure.wotr.entity.ai.goals;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;

public interface IRangedAttack {

	public ProjectileEntity getProjectile(World world, double d2, double d3, double d4);

	public AttackSound getDefaultAttackSound();

}
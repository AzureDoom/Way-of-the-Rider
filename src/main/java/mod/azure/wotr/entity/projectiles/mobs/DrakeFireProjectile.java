package mod.azure.wotr.entity.projectiles.mobs;

import mod.azure.wotr.blocks.tile.TickingLightEntity;
import mod.azure.wotr.client.WoTRClientMod.EntityPacket;
import mod.azure.wotr.config.WoTRConfig;
import mod.azure.wotr.entity.DrakeEntity;
import mod.azure.wotr.registry.WoTRBlocks;
import mod.azure.wotr.registry.WoTREntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class DrakeFireProjectile extends ExplosiveProjectileEntity {

	protected int timeInAir;
	protected boolean inAir;
	private int ticksInAir;
	private BlockPos lightBlockPos = null;

	public DrakeFireProjectile(EntityType<? extends DrakeFireProjectile> entityType, World world) {
		super(entityType, world);
	}

	public DrakeFireProjectile(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(WoTREntities.DRAKE_FIRE, shooter, accelX, accelY, accelZ, worldIn);
	}

	public DrakeFireProjectile(World worldIn, double x, double y, double z, double accelX, double accelY,
			double accelZ) {
		super(WoTREntities.DRAKE_FIRE, x, y, z, accelX, accelY, accelZ, worldIn);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return EntityPacket.createPacket(this);
	}

	@Override
	public void setVelocity(double x, double y, double z, float speed, float divergence) {
		super.setVelocity(x, y, z, speed, divergence);
		this.ticksInAir = 0;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putShort("life", (short) this.ticksInAir);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		this.ticksInAir = tag.getShort("life");
	}

	@Override
	public boolean hasNoGravity() {
		if (this.isSubmergedInWater()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.ticksInAir >= 80) {
			this.remove(Entity.RemovalReason.DISCARDED);
		}
		boolean isInsideWaterBlock = world.isWater(getBlockPos());
		spawnLightSource(isInsideWaterBlock);
		if (this.world.isClient && this.age > 5) {
			double d2 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getWidth() * 0.5D;
			double e2 = this.getY() + 0.05D + this.random.nextDouble();
			double f2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getWidth() * 0.5D;
			this.world.addParticle(ParticleTypes.FLAME, true, d2, e2, f2, 0, 0, 0);
			this.world.addParticle(ParticleTypes.SMOKE, true, d2, e2, f2, 0, 0, 0);
		}
	}

	private void spawnLightSource(boolean isInWaterBlock) {
		if (lightBlockPos == null) {
			lightBlockPos = findFreeSpace(world, getBlockPos(), 2);
			if (lightBlockPos == null)
				return;
			world.setBlockState(lightBlockPos, WoTRBlocks.TICKING_LIGHT_BLOCK.getDefaultState());
		} else if (checkDistance(lightBlockPos, getBlockPos(), 2)) {
			BlockEntity blockEntity = world.getBlockEntity(lightBlockPos);
			if (blockEntity instanceof TickingLightEntity) {
				((TickingLightEntity) blockEntity).refresh(isInWaterBlock ? 20 : 0);
			} else
				lightBlockPos = null;
		} else
			lightBlockPos = null;
	}

	private boolean checkDistance(BlockPos blockPosA, BlockPos blockPosB, int distance) {
		return Math.abs(blockPosA.getX() - blockPosB.getX()) <= distance
				&& Math.abs(blockPosA.getY() - blockPosB.getY()) <= distance
				&& Math.abs(blockPosA.getZ() - blockPosB.getZ()) <= distance;
	}

	private BlockPos findFreeSpace(World world, BlockPos blockPos, int maxDistance) {
		if (blockPos == null)
			return null;

		int[] offsets = new int[maxDistance * 2 + 1];
		offsets[0] = 0;
		for (int i = 2; i <= maxDistance * 2; i += 2) {
			offsets[i - 1] = i / 2;
			offsets[i] = -i / 2;
		}
		for (int x : offsets)
			for (int y : offsets)
				for (int z : offsets) {
					BlockPos offsetPos = blockPos.add(x, y, z);
					BlockState state = world.getBlockState(offsetPos);
					if (state.isAir() || state.getBlock().equals(WoTRBlocks.TICKING_LIGHT_BLOCK))
						return offsetPos;
				}

		return null;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		BlockPos blockPos;
		super.onBlockHit(blockHitResult);
		if (this.world.isClient) {
			return;
		}
		Entity entity = this.getOwner();
		if ((!(entity instanceof MobEntity) || this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
				&& this.world.isAir(blockPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide()))) {
			this.world.setBlockState(blockPos, AbstractFireBlock.getState(this.world, blockPos));
		}
	}

	@Override
	protected void onCollision(HitResult result) {
		super.onCollision(result);
		if (!this.world.isClient) {
			this.remove(Entity.RemovalReason.DISCARDED);
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (!this.world.isClient) {
			Entity entity = entityHitResult.getEntity();
			if (!(entity instanceof DrakeEntity))
				entity.damage(DamageSource.magic(this, entity), WoTRConfig.drake_ranged);
			this.remove(Entity.RemovalReason.DISCARDED);
			entity.setOnFireFor(15);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		return true;
	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}
}
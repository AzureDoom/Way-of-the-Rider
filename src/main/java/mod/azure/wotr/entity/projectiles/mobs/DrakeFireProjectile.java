package mod.azure.wotr.entity.projectiles.mobs;

import mod.azure.azurelib.AzureLibMod;
import mod.azure.azurelib.entities.TickingLightEntity;
import mod.azure.azurelib.network.packet.EntityPacket;
import mod.azure.wotr.config.WoTRConfig;
import mod.azure.wotr.entity.DrakeEntity;
import mod.azure.wotr.registry.WoTREntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class DrakeFireProjectile extends AbstractHurtingProjectile {

	protected int timeInAir;
	protected boolean inAir;
	private int ticksInAir;
	private BlockPos lightBlockPos = null;

	public DrakeFireProjectile(EntityType<? extends DrakeFireProjectile> entityType, Level world) {
		super(entityType, world);
	}

	public DrakeFireProjectile(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(WoTREntities.DRAKE_FIRE, shooter, accelX, accelY, accelZ, worldIn);
	}

	public DrakeFireProjectile(Level worldIn, double x, double y, double z, double accelX, double accelY,
			double accelZ) {
		super(WoTREntities.DRAKE_FIRE, x, y, z, accelX, accelY, accelZ, worldIn);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return EntityPacket.createPacket(this);
	}

	@Override
	public void shoot(double x, double y, double z, float speed, float divergence) {
		super.shoot(x, y, z, speed, divergence);
		this.ticksInAir = 0;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putShort("life", (short) this.ticksInAir);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.ticksInAir = tag.getShort("life");
	}

	@Override
	public boolean isNoGravity() {
		if (this.isUnderWater()) {
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
		boolean isInsideWaterBlock = level.isWaterAt(blockPosition());
		spawnLightSource(isInsideWaterBlock);
		if (this.level.isClientSide() && this.tickCount > 5) {
			double d2 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D;
			double e2 = this.getY() + 0.05D + this.random.nextDouble();
			double f2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D;
			this.level.addParticle(ParticleTypes.FLAME, true, d2, e2, f2, 0, 0, 0);
			this.level.addParticle(ParticleTypes.SMOKE, true, d2, e2, f2, 0, 0, 0);
		}
	}

	private void spawnLightSource(boolean isInWaterBlock) {
		if (lightBlockPos == null) {
			lightBlockPos = findFreeSpace(level, blockPosition(), 2);
			if (lightBlockPos == null)
				return;
			level.setBlockAndUpdate(lightBlockPos, AzureLibMod.TICKING_LIGHT_BLOCK.defaultBlockState());
		} else if (checkDistance(lightBlockPos, blockPosition(), 2)) {
			BlockEntity blockEntity = level.getBlockEntity(lightBlockPos);
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

	private BlockPos findFreeSpace(Level world, BlockPos blockPos, int maxDistance) {
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
					BlockPos offsetPos = blockPos.offset(x, y, z);
					BlockState state = world.getBlockState(offsetPos);
					if (state.isAir() || state.getBlock().equals(AzureLibMod.TICKING_LIGHT_BLOCK))
						return offsetPos;
				}

		return null;
	}

	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		BlockPos blockPos;
		super.onHitBlock(blockHitResult);
		if (this.level.isClientSide())
			return;
		Entity entity = this.getOwner();
		if ((!(entity instanceof Mob) || this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && this.level
				.isEmptyBlock(blockPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection()))) {
			this.level.setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level, blockPos));
		}
		this.remove(Entity.RemovalReason.DISCARDED);
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		if (this.level.isClientSide())
			return;
		Entity entity = entityHitResult.getEntity();
		if (!(entity instanceof DrakeEntity))
			entity.hurt(DamageSource.indirectMagic(this, entity), WoTRConfig.drake_ranged);
		this.remove(Entity.RemovalReason.DISCARDED);
		entity.setSecondsOnFire(15);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true;
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}
}
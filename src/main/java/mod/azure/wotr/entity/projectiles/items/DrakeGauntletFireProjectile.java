package mod.azure.wotr.entity.projectiles.items;

import mod.azure.azurelib.AzureLibMod;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.entities.TickingLightEntity;
import mod.azure.azurelib.network.packet.EntityPacket;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.wotr.registry.WoTREntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class DrakeGauntletFireProjectile extends AbstractArrow implements GeoEntity {

	protected int timeInAir;
	protected boolean inAir;
	private int ticksInAir;
	private LivingEntity shooter;
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private BlockPos lightBlockPos = null;
	private int idleTicks = 0;

	public DrakeGauntletFireProjectile(EntityType<? extends DrakeGauntletFireProjectile> entityType, Level world) {
		super(entityType, world);
		this.pickup = AbstractArrow.Pickup.DISALLOWED;
	}

	public DrakeGauntletFireProjectile(Level world, LivingEntity owner) {
		super(WoTREntities.DRAKE_GAUNTLET_FIRE, owner, world);
		this.shooter = owner;
	}

	protected DrakeGauntletFireProjectile(EntityType<? extends DrakeGauntletFireProjectile> type, double x, double y, double z, Level world) {
		this(type, world);
	}

	protected DrakeGauntletFireProjectile(EntityType<? extends DrakeGauntletFireProjectile> type, LivingEntity owner, Level world) {
		this(type, owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ(), world);
		this.setOwner(owner);
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
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return EntityPacket.createPacket(this);
	}

	@Override
	public void tickDespawn() {
		++this.ticksInAir;
		if (this.ticksInAir >= 40)
			this.remove(Entity.RemovalReason.DISCARDED);
	}

	@Override
	public void shoot(double x, double y, double z, float speed, float divergence) {
		super.shoot(x, y, z, speed, divergence);
		this.ticksInAir = 0;
	}

	@Override
	protected void doPostHurtEffects(LivingEntity living) {
		super.doPostHurtEffects(living);
		if (!(living instanceof Player)) {
			living.setDeltaMovement(0, 0, 0);
			living.invulnerableTime = 0;
		}
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
	public void tick() {
		var idleOpt = 100;
		if (getDeltaMovement().lengthSqr() < 0.01)
			idleTicks++;
		else
			idleTicks = 0;
		if (idleOpt <= 0 || idleTicks < idleOpt)
			super.tick();
		++this.ticksInAir;
		if (this.ticksInAir >= 40)
			this.remove(Entity.RemovalReason.DISCARDED);
		var isInsideWaterBlock = level().isWaterAt(blockPosition());
		spawnLightSource(isInsideWaterBlock);
		if (this.level().isClientSide()) {
			this.level().addParticle(ParticleTypes.FLAME, true, this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D, this.getY() + 0.05D + this.random.nextDouble(), this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D, 0, 0, 0);
			this.level().addParticle(ParticleTypes.SMOKE, true, this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D, this.getY() + 0.05D + this.random.nextDouble(), this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D, 0, 0, 0);
		}
		var aabb = new AABB(this.blockPosition().above()).inflate(1D, 5D, 1D);
		this.getCommandSenderWorld().getEntities(this, aabb).forEach(e -> {
			if (e.isAlive() && e instanceof LivingEntity) {
				e.hurt(damageSources().arrow(this, this.shooter), 3);
				if (!(e instanceof DrakeGauntletFireProjectile || this.getOwner() instanceof Player))
					e.setRemainingFireTicks(90);
			}
		});
	}

	public void initFromStack(ItemStack stack) {
		if (stack.getItem() == Items.AIR) {
		}
	}

	@Override
	public boolean isNoGravity() {
		if (this.isUnderWater())
			return false;
		return true;
	}

	public SoundEvent hitSound = this.getDefaultHitGroundSoundEvent();

	@Override
	public void setSoundEvent(SoundEvent soundIn) {
		this.hitSound = soundIn;
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.FIRE_AMBIENT;
	}

	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		if (!this.level().isClientSide()) {
			var entity = this.getOwner();
			if (entity == null || !(entity instanceof Mob) || this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
				var blockPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
				if (this.level().isEmptyBlock(blockPos))
					this.level().setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level(), blockPos));
			}
			this.remove(Entity.RemovalReason.DISCARDED);
		}
		this.setSoundEvent(SoundEvents.FIRE_AMBIENT);
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		if (!this.level().isClientSide())
			this.remove(Entity.RemovalReason.DISCARDED);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(Items.AIR);
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

	private void spawnLightSource(boolean isInWaterBlock) {
		if (lightBlockPos == null) {
			lightBlockPos = findFreeSpace(level(), blockPosition(), 2);
			if (lightBlockPos == null)
				return;
			level().setBlockAndUpdate(lightBlockPos, AzureLibMod.TICKING_LIGHT_BLOCK.defaultBlockState());
		} else if (checkDistance(lightBlockPos, blockPosition(), 2)) {
			var blockEntity = level().getBlockEntity(lightBlockPos);
			if (blockEntity instanceof TickingLightEntity)
				((TickingLightEntity) blockEntity).refresh(isInWaterBlock ? 20 : 0);
			else
				lightBlockPos = null;
		} else
			lightBlockPos = null;
	}

	private boolean checkDistance(BlockPos blockPosA, BlockPos blockPosB, int distance) {
		return Math.abs(blockPosA.getX() - blockPosB.getX()) <= distance && Math.abs(blockPosA.getY() - blockPosB.getY()) <= distance && Math.abs(blockPosA.getZ() - blockPosB.getZ()) <= distance;
	}

	private BlockPos findFreeSpace(Level world, BlockPos blockPos, int maxDistance) {
		if (blockPos == null)
			return null;

		var offsets = new int[maxDistance * 2 + 1];
		offsets[0] = 0;
		for (var i = 2; i <= maxDistance * 2; i += 2) {
			offsets[i - 1] = i / 2;
			offsets[i] = -i / 2;
		}
		for (var x : offsets)
			for (var y : offsets)
				for (var z : offsets) {
					var offsetPos = blockPos.offset(x, y, z);
					var state = world.getBlockState(offsetPos);
					if (state.isAir() || state.getBlock().equals(AzureLibMod.TICKING_LIGHT_BLOCK))
						return offsetPos;
				}

		return null;
	}
}

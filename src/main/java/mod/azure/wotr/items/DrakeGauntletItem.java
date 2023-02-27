package mod.azure.wotr.items;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.netty.buffer.Unpooled;
import mod.azure.azurelib.AzureLibMod;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.entities.TickingLightEntity;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.client.WoTRClientMod;
import mod.azure.wotr.client.render.items.DrakeGauntletRender;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class DrakeGauntletItem extends SwordItem implements GeoItem {

	private BlockPos lightBlockPos = null;
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

	public DrakeGauntletItem() {
		super(Tiers.DIAMOND, 20, -3.0F, new Item.Properties().stacksTo(1).durability(11));

		// Register our item as server-side handled.
		// This enables both animation data syncing and server-side animation triggering
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}

	public void removeAmmo(Item ammo, Player playerEntity) {
		if (!playerEntity.isCreative()) {
			for (ItemStack item : playerEntity.getInventory().offhand) {
				if (item.getItem() == ammo) {
					item.setDamageValue(item.getDamageValue() - 1);
					break;
				}
				for (ItemStack item1 : playerEntity.getInventory().items) {
					if (item1.getItem() == ammo) {
						item.setDamageValue(item.getDamageValue() - 1);
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
		tooltip.add(Component.translatable(
				"Fuel: " + (stack.getMaxDamage() - stack.getDamageValue() - 1) + " / " + (stack.getMaxDamage() - 1))
				.withStyle(ChatFormatting.ITALIC));
		tooltip.add(Component.translatable(WoTRMod.MODID + ".ammo.reloaddrake").withStyle(ChatFormatting.ITALIC));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		user.startUsingItem(hand);
		return InteractionResultHolder.consume(itemStack);
	}

	protected void spawnLightSource(Entity entity, boolean isInWaterBlock) {
		if (lightBlockPos == null) {
			lightBlockPos = findFreeSpace(entity.level, entity.blockPosition(), 2);
			if (lightBlockPos == null)
				return;
			entity.level.setBlockAndUpdate(lightBlockPos, AzureLibMod.TICKING_LIGHT_BLOCK.defaultBlockState());
		} else if (checkDistance(lightBlockPos, entity.blockPosition(), 2)) {
			BlockEntity blockEntity = entity.level.getBlockEntity(lightBlockPos);
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

	// Register our animation controllers
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "shoot_controller", event -> PlayState.CONTINUE)
				.triggerableAnim("firing", RawAnimation.begin().then("firing", LoopType.PLAY_ONCE)));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (world.isClientSide()) {
			if (((Player) entity).getMainHandItem().getItem() instanceof DrakeGauntletItem) {
				if (WoTRClientMod.reload.isDown() && selected) {
					FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
					passedData.writeBoolean(true);
					ClientPlayNetworking.send(WoTRMod.RELOAD, passedData);
					world.playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(),
							SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.5F);
				}
			}
		}
	}

	public void reload(Player user, InteractionHand hand) {
		if (user.getItemInHand(hand).getItem() instanceof DrakeGauntletItem) {
			while (!user.isCreative() && user.getItemInHand(hand).getDamageValue() != 0
					&& user.getInventory().countItem(Items.FLINT_AND_STEEL) > 0) {
				removeAmmo(Items.FLINT_AND_STEEL, user);
				user.getItemInHand(hand).hurtAndBreak(-10, user, s -> user.broadcastBreakEvent(hand));
				user.getItemInHand(hand).setPopTime(3);
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int remainingUseTicks) {
		if (entityLiving instanceof Player) {
			Player playerentity = (Player) entityLiving;
			if (stack.getDamageValue() < (stack.getMaxDamage() - 1)) {
				playerentity.getCooldowns().addCooldown(this, 25);
				stack.hurtAndBreak(1, entityLiving, p -> p.broadcastBreakEvent(entityLiving.getUsedItemHand()));
			}
		}
	}

	@Override
	public void onUseTick(Level worldIn, LivingEntity entityLiving, ItemStack stack, int count) {
		if (entityLiving instanceof Player) {
			Player playerentity = (Player) entityLiving;
			if (stack.getDamageValue() < (stack.getMaxDamage() - 1)) {
				if (!worldIn.isClientSide()) {
					DrakeGauntletFireProjectile abstractarrowentity = createArrow(worldIn, stack, playerentity);
					abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F,
							0.25F * 3.0F, 2.0F);
					abstractarrowentity.moveTo(entityLiving.getX(), entityLiving.getY(0.5), entityLiving.getZ(), 0, 0);
					abstractarrowentity.tickCount = 30;
					worldIn.addFreshEntity(abstractarrowentity);
					triggerAnim(playerentity, GeoItem.getOrAssignId(stack, (ServerLevel)worldIn), "shoot_controller", "firing");
					worldIn.playSound((Player) null, playerentity.getX(), playerentity.getY(), playerentity.getZ(),
							SoundEvents.CAMPFIRE_CRACKLE, SoundSource.PLAYERS, 1.0F,
							1.0F / (worldIn.random.nextFloat() * 0.4F + 1.2F) + 1F * 0.5F);
				}
				boolean isInsideWaterBlock = playerentity.level.isWaterAt(playerentity.blockPosition());
				spawnLightSource(entityLiving, isInsideWaterBlock);
			}
		}
	}

	public DrakeGauntletFireProjectile createArrow(Level worldIn, ItemStack stack, LivingEntity shooter) {
		DrakeGauntletFireProjectile arrowentity = new DrakeGauntletFireProjectile(worldIn, shooter);
		return arrowentity;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity miner) {
		if (miner instanceof Player) {
			Player playerentity = (Player) miner;
			if (stack.getDamageValue() < (stack.getMaxDamage() - 1)) {
				if (!playerentity.getCooldowns().isOnCooldown(this)
						&& playerentity.getMainHandItem().getItem() instanceof DrakeGauntletItem) {
					playerentity.getCooldowns().addCooldown(this, 20);
					final AABB aabb = new AABB(playerentity.blockPosition().above()).inflate(2D, 1D, 2D);
					playerentity.getCommandSenderWorld().getEntities(playerentity, aabb)
							.forEach(e -> doDamage(playerentity, e));
					stack.hurtAndBreak(1, playerentity, p -> p.broadcastBreakEvent(playerentity.getUsedItemHand()));
				}
			}
		}
		return super.hurtEnemy(stack, target, miner);
	}

	private void doDamage(LivingEntity user, Entity target) {
		if (target instanceof LivingEntity) {
			target.invulnerableTime = 0;
			target.hurt(DamageSource.playerAttack((Player) user), 9F);
		}
	}
	
	@Override
	public void createRenderer(Consumer<Object> consumer) {
		consumer.accept(new RenderProvider() {
			private final DrakeGauntletRender renderer = new DrakeGauntletRender();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return this.renderer;
			}
		});
	}

	@Override
	public Supplier<Object> getRenderProvider() {
		return this.renderProvider;
	}

}

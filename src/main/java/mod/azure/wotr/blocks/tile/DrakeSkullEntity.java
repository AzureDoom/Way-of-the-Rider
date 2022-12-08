package mod.azure.wotr.blocks.tile;

import java.util.Random;

import mod.azure.wotr.registry.WoTREntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DrakeSkullEntity extends BlockEntity implements GeoBlockEntity {

	protected final Random random = new Random();
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public DrakeSkullEntity(BlockPos pos, BlockState state) {
		super(WoTREntities.DRAKE_SKULL, pos, state);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, state -> {
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
package mod.azure.wotr.client.models.items;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrakeSkullBlockModel extends AnimatedGeoModel<DrakeSkullEntity> {
	@Override
	public ResourceLocation getAnimationResource(DrakeSkullEntity entity) {
		return new ResourceLocation(WoTRMod.MODID, "animations/drake_skull.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DrakeSkullEntity animatable) {
		return new ResourceLocation(WoTRMod.MODID, "geo/drake_skull.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DrakeSkullEntity entity) {
		return new ResourceLocation(WoTRMod.MODID, "textures/blocks/drake_skull.png");
	}
}

package mod.azure.wotr.client.models.items;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DrakeSkullBlockModel extends GeoModel<DrakeSkullEntity> {
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
		return new ResourceLocation(WoTRMod.MODID, "textures/block/drake_skull.png");
	}

	@Override
	public RenderType getRenderType(DrakeSkullEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}

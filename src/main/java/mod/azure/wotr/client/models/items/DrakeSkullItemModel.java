package mod.azure.wotr.client.models.items;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.GeckoBlockItem;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.GeoModel;

public class DrakeSkullItemModel extends GeoModel<GeckoBlockItem> {
	@Override
	public ResourceLocation getAnimationResource(GeckoBlockItem entity) {
		return new ResourceLocation(WoTRMod.MODID, "animations/drake_skull.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GeckoBlockItem animatable) {
		return new ResourceLocation(WoTRMod.MODID, "geo/drake_skull.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GeckoBlockItem entity) {
		return new ResourceLocation(WoTRMod.MODID, "textures/block/drake_skull.png");
	}
}

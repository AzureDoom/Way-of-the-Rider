package mod.azure.wotr.client.models.items;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.DrakeGauntletItem;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.GeoModel;

public class DrakeGauntletModel extends GeoModel<DrakeGauntletItem> {
	@Override
	public ResourceLocation getModelResource(DrakeGauntletItem object) {
		return new ResourceLocation(WoTRMod.MODID, "geo/drake_gauntlet.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DrakeGauntletItem object) {
		return new ResourceLocation(WoTRMod.MODID, "textures/item/drake_gauntlet.png");
	}

	@Override
	public ResourceLocation getAnimationResource(DrakeGauntletItem animatable) {
		return new ResourceLocation(WoTRMod.MODID, "animations/drake_gauntlet.animation.json");
	}
}

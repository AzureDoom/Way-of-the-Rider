package mod.azure.wotr.client.models.items;

import mod.azure.azurelib.model.GeoModel;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.DrakeGauntletItem;
import net.minecraft.resources.ResourceLocation;

public class DrakeGauntletModel extends GeoModel<DrakeGauntletItem> {
	@Override
	public ResourceLocation getModelResource(DrakeGauntletItem object) {
		return WoTRMod.modResource("geo/drake_gauntlet.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DrakeGauntletItem object) {
		return WoTRMod.modResource("textures/item/drake_gauntlet.png");
	}

	@Override
	public ResourceLocation getAnimationResource(DrakeGauntletItem animatable) {
		return WoTRMod.modResource("animations/drake_gauntlet.animation.json");
	}
}

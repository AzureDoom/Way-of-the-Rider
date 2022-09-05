package mod.azure.wotr.client.models.items;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.GeckoBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrakeSkullItemModel extends AnimatedGeoModel<GeckoBlockItem> {
	@Override
	public Identifier getAnimationResource(GeckoBlockItem entity) {
		return new Identifier(WoTRMod.MODID, "animations/drake_skull.animation.json");
	}

	@Override
	public Identifier getModelResource(GeckoBlockItem animatable) {
		return new Identifier(WoTRMod.MODID, "geo/drake_skull.geo.json");
	}

	@Override
	public Identifier getTextureResource(GeckoBlockItem entity) {
		return new Identifier(WoTRMod.MODID, "textures/blocks/drake_skull.png");
	}
}

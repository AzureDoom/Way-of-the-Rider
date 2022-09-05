package mod.azure.wotr.client.models.items;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.DrakeGauntletItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrakeGauntletModel extends AnimatedGeoModel<DrakeGauntletItem> {
	@Override
	public Identifier getModelResource(DrakeGauntletItem object) {
		return new Identifier(WoTRMod.MODID, "geo/drake_gauntlet.geo.json");
	}

	@Override
	public Identifier getTextureResource(DrakeGauntletItem object) {
		return new Identifier(WoTRMod.MODID, "textures/items/drake_gauntlet.png");
	}

	@Override
	public Identifier getAnimationResource(DrakeGauntletItem animatable) {
		return new Identifier(WoTRMod.MODID, "animations/drake_gauntlet.animation.json");
	}
}

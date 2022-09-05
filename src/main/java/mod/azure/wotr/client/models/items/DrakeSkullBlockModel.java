package mod.azure.wotr.client.models.items;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrakeSkullBlockModel extends AnimatedGeoModel<DrakeSkullEntity> {
	@Override
	public Identifier getAnimationResource(DrakeSkullEntity entity) {
		return new Identifier(WoTRMod.MODID, "animations/drake_skull.animation.json");
	}

	@Override
	public Identifier getModelResource(DrakeSkullEntity animatable) {
		return new Identifier(WoTRMod.MODID, "geo/drake_skull.geo.json");
	}

	@Override
	public Identifier getTextureResource(DrakeSkullEntity entity) {
		return new Identifier(WoTRMod.MODID, "textures/blocks/drake_skull.png");
	}
}

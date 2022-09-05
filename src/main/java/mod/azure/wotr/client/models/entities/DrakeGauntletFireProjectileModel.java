package mod.azure.wotr.client.models.entities;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrakeGauntletFireProjectileModel extends AnimatedGeoModel<DrakeGauntletFireProjectile> {
	@Override
	public Identifier getModelResource(DrakeGauntletFireProjectile object) {
		return new Identifier(WoTRMod.MODID, "geo/drakefire.geo.json");
	}

	@Override
	public Identifier getTextureResource(DrakeGauntletFireProjectile object) {
		return new Identifier(WoTRMod.MODID, "textures/items/empty.png");
	}

	@Override
	public Identifier getAnimationResource(DrakeGauntletFireProjectile animatable) {
		return new Identifier(WoTRMod.MODID, "animations/drakefire.animation.json");
	}
}

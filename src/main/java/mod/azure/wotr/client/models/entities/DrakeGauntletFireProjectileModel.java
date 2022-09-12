package mod.azure.wotr.client.models.entities;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrakeGauntletFireProjectileModel extends AnimatedGeoModel<DrakeGauntletFireProjectile> {
	@Override
	public ResourceLocation getModelResource(DrakeGauntletFireProjectile object) {
		return new ResourceLocation(WoTRMod.MODID, "geo/drakefire.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DrakeGauntletFireProjectile object) {
		return new ResourceLocation(WoTRMod.MODID, "textures/items/empty.png");
	}

	@Override
	public ResourceLocation getAnimationResource(DrakeGauntletFireProjectile animatable) {
		return new ResourceLocation(WoTRMod.MODID, "animations/drakefire.animation.json");
	}
}

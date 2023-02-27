package mod.azure.wotr.client.models.entities;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.GeoModel;

public class DrakeGauntletFireProjectileModel extends GeoModel<DrakeGauntletFireProjectile> {
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

	@Override
	public RenderType getRenderType(DrakeGauntletFireProjectile animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}

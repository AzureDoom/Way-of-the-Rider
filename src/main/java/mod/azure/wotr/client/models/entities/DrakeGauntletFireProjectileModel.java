package mod.azure.wotr.client.models.entities;

import mod.azure.azurelib.model.GeoModel;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class DrakeGauntletFireProjectileModel extends GeoModel<DrakeGauntletFireProjectile> {
    @Override
    public ResourceLocation getModelResource(DrakeGauntletFireProjectile object) {
        return WoTRMod.modResource("geo/drakefire.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DrakeGauntletFireProjectile object) {
        return WoTRMod.modResource("textures/items/empty.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DrakeGauntletFireProjectile animatable) {
        return WoTRMod.modResource("animations/drakefire.animation.json");
    }

    @Override
    public RenderType getRenderType(DrakeGauntletFireProjectile animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}

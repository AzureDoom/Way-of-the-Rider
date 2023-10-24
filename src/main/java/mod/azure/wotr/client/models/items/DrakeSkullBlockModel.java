package mod.azure.wotr.client.models.items;

import mod.azure.azurelib.model.GeoModel;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class DrakeSkullBlockModel extends GeoModel<DrakeSkullEntity> {
    @Override
    public ResourceLocation getAnimationResource(DrakeSkullEntity entity) {
        return WoTRMod.modResource("animations/drake_skull.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DrakeSkullEntity animatable) {
        return WoTRMod.modResource("geo/drake_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DrakeSkullEntity entity) {
        return WoTRMod.modResource("textures/block/drake_skull.png");
    }

    @Override
    public RenderType getRenderType(DrakeSkullEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}

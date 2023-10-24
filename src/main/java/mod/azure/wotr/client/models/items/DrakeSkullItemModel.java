package mod.azure.wotr.client.models.items;

import mod.azure.azurelib.model.GeoModel;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.GeckoBlockItem;
import net.minecraft.resources.ResourceLocation;

public class DrakeSkullItemModel extends GeoModel<GeckoBlockItem> {
    @Override
    public ResourceLocation getAnimationResource(GeckoBlockItem entity) {
        return WoTRMod.modResource("animations/drake_skull.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(GeckoBlockItem animatable) {
        return WoTRMod.modResource("geo/drake_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeckoBlockItem entity) {
        return WoTRMod.modResource("textures/block/drake_skull.png");
    }
}

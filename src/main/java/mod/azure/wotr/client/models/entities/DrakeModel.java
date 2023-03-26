package mod.azure.wotr.client.models.entities;

import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class DrakeModel extends GeoModel<DrakeEntity> {

	@Override
	public ResourceLocation getModelResource(DrakeEntity object) {
		return WoTRMod.modResource("geo/drake.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DrakeEntity object) {
		return WoTRMod.modResource("textures/entity/drake_" + object.getVariant() + ".png");
	}

	@Override
	public ResourceLocation getAnimationResource(DrakeEntity object) {
		return WoTRMod.modResource("animations/drake.animation.json");
	}

	@Override
	public void setCustomAnimations(DrakeEntity animatable, long instanceId, AnimationState<DrakeEntity> animationState) {
		super.setCustomAnimations(animatable, instanceId, animationState);
		var neck = getAnimationProcessor().getBone("neck");
		var entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
		if (neck != null)
			neck.setRotY((entityData.netHeadYaw() * (((float) Math.PI) / 180F)));
	}

	@Override
	public RenderType getRenderType(DrakeEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
package mod.azure.wotr.client.models.entities;

import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.LungSerpentEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class LungSerpentModel extends GeoModel<LungSerpentEntity> {

	@Override
	public ResourceLocation getModelResource(LungSerpentEntity object) {
		return WoTRMod.modResource("geo/lung_serpent.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(LungSerpentEntity object) {
		return WoTRMod.modResource("textures/entity/lung_serpent_" + object.getVariant() + ".png");
	}

	@Override
	public ResourceLocation getAnimationResource(LungSerpentEntity object) {
		return WoTRMod.modResource("animations/lung_serpent.animation.json");
	}

	@Override
	public void setCustomAnimations(LungSerpentEntity animatable, long instanceId, AnimationState<LungSerpentEntity> animationState) {
		super.setCustomAnimations(animatable, instanceId, animationState);
		var neck = getAnimationProcessor().getBone("neck");
		var entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
		if (neck != null)
			neck.setRotY((entityData.netHeadYaw() * (((float) Math.PI) / 180F)));
	}

	@Override
	public RenderType getRenderType(LungSerpentEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
package mod.azure.wotr.client.models.entities;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DrakeModel extends GeoModel<DrakeEntity> {

	@Override
	public ResourceLocation getModelResource(DrakeEntity object) {
		return new ResourceLocation(WoTRMod.MODID, "geo/drake.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DrakeEntity object) {
		return new ResourceLocation(WoTRMod.MODID, "textures/entity/drake_" + object.getVariant() + ".png");
	}

	@Override
	public ResourceLocation getAnimationResource(DrakeEntity object) {
		return new ResourceLocation(WoTRMod.MODID, "animations/drake.animation.json");
	}

	@Override
	public void setCustomAnimations(DrakeEntity animatable, long instanceId,
			AnimationState<DrakeEntity> animationState) {
		super.setCustomAnimations(animatable, instanceId, animationState);
		CoreGeoBone neck = getAnimationProcessor().getBone("neck");
		EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
		if (neck != null) {
			neck.setRotY((entityData.netHeadYaw() * (((float) Math.PI) / 180F)));
		}
	}

	@Override
	public RenderType getRenderType(DrakeEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}
}
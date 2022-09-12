package mod.azure.wotr.client.models.entities;

import com.mojang.math.Vector3f;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DrakeModel extends AnimatedTickingGeoModel<DrakeEntity> {

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
	public void setLivingAnimations(DrakeEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("neck");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(
					Vector3f.XP.rotation((extraData.headPitch + 15) * ((float) Math.PI / 180F)).i());
			head.setRotationY(
					Vector3f.YP.rotation(extraData.netHeadYaw * ((float) Math.PI / 340F)).j());
		}
	}
}
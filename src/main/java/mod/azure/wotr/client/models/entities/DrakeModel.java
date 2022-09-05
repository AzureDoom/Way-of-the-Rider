package mod.azure.wotr.client.models.entities;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DrakeModel extends AnimatedTickingGeoModel<DrakeEntity> {

	@Override
	public Identifier getModelResource(DrakeEntity object) {
		return new Identifier(WoTRMod.MODID, "geo/drake.geo.json");
	}

	@Override
	public Identifier getTextureResource(DrakeEntity object) {
		return new Identifier(WoTRMod.MODID, "textures/entity/drake_" + object.getVariant() + ".png");
	}

	@Override
	public Identifier getAnimationResource(DrakeEntity object) {
		return new Identifier(WoTRMod.MODID, "animations/drake.animation.json");
	}

	@Override
	public void setLivingAnimations(DrakeEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("neck");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(
					Vec3f.POSITIVE_X.getRadialQuaternion((extraData.headPitch + 15) * ((float) Math.PI / 180F)).getX());
			head.setRotationY(
					Vec3f.POSITIVE_Y.getRadialQuaternion(extraData.netHeadYaw * ((float) Math.PI / 340F)).getY());
		}
	}
}
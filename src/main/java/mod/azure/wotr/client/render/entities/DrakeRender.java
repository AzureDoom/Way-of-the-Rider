package mod.azure.wotr.client.render.entities;

import mod.azure.wotr.client.models.entities.DrakeModel;
import mod.azure.wotr.client.render.entities.layer.DrakeArmorLayer;
import mod.azure.wotr.client.render.entities.layer.DrakeSaddleLayer;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DrakeRender extends GeoEntityRenderer<DrakeEntity> {

	public DrakeRender(EntityRendererFactory.Context renderManagerIn) {
		super(renderManagerIn, new DrakeModel());
		this.addLayer(new DrakeArmorLayer(this));
		this.addLayer(new DrakeSaddleLayer(this));
		this.shadowRadius = 0.7F;
	}

	@Override
	public RenderLayer getRenderType(DrakeEntity animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
	}

}
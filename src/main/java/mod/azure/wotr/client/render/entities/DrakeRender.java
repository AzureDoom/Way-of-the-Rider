package mod.azure.wotr.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.azure.wotr.client.models.entities.DrakeModel;
import mod.azure.wotr.client.render.entities.layer.DrakeArmorLayer;
import mod.azure.wotr.client.render.entities.layer.DrakeSaddleLayer;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DrakeRender extends GeoEntityRenderer<DrakeEntity> {

	public DrakeRender(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn, new DrakeModel());
		this.addLayer(new DrakeArmorLayer(this));
		this.addLayer(new DrakeSaddleLayer(this));
		this.shadowRadius = 0.7F;
	}
	
	@Override
	public void render(DrakeEntity entity, float entityYaw, float partialTicks, PoseStack stack,
			MultiBufferSource bufferIn, int packedLightIn) {
		float scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 4.0f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

	@Override
	public RenderType getRenderType(DrakeEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

	@Override
	public ResourceLocation getTextureLocation(DrakeEntity var1) {
		return this.getTextureResource(var1);
	}

}
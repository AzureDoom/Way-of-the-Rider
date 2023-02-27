package mod.azure.wotr.client.render.entities;

import com.mojang.blaze3d.vertex.PoseStack;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.wotr.client.models.entities.LungSerpentModel;
import mod.azure.wotr.client.render.entities.layer.LungSerpentSaddleLayer;
import mod.azure.wotr.entity.LungSerpentEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class LungSerpentRender extends GeoEntityRenderer<LungSerpentEntity> {

	public LungSerpentRender(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn, new LungSerpentModel());
		this.addRenderLayer(new LungSerpentSaddleLayer(this));
		this.shadowRadius = 0.7F;
	}

	@Override
	public void render(LungSerpentEntity entity, float entityYaw, float partialTicks, PoseStack stack,
			MultiBufferSource bufferIn, int packedLightIn) {
		float scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 4.0f);
		stack.scale(scaleFactor, scaleFactor, scaleFactor);
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}

}
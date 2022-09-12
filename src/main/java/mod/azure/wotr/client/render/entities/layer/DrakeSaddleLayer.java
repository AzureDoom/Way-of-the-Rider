package mod.azure.wotr.client.render.entities.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class DrakeSaddleLayer extends GeoLayerRenderer<DrakeEntity> {

	public DrakeSaddleLayer(IGeoRenderer<DrakeEntity> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
			DrakeEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		RenderType cameo = RenderType
				.armorCutoutNoCull(new ResourceLocation(WoTRMod.MODID, "textures/entity/layer/drake_saddle.png"));
		matrixStackIn.pushPose();
		matrixStackIn.scale(1.0f, 1.0f, 1.0f);
		matrixStackIn.translate(0.0d, 0.0d, 0.0d);
		if (entitylivingbaseIn.isSaddled())
			this.getRenderer().render(
					this.getEntityModel().getModel(new ResourceLocation(WoTRMod.MODID, "geo/drake.geo.json")),
					entitylivingbaseIn, partialTicks, cameo, matrixStackIn, bufferIn, bufferIn.getBuffer(cameo),
					packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
		matrixStackIn.popPose();
	}
}
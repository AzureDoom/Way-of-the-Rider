package mod.azure.wotr.client.render.entities.layer;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class DrakeSaddleLayer extends GeoLayerRenderer<DrakeEntity> {

	public DrakeSaddleLayer(IGeoRenderer<DrakeEntity> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn,
			DrakeEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {
		RenderLayer cameo = RenderLayer
				.getArmorCutoutNoCull(new Identifier(WoTRMod.MODID, "textures/entity/layer/drake_saddle.png"));
		matrixStackIn.push();
		matrixStackIn.scale(1.0f, 1.0f, 1.0f);
		matrixStackIn.translate(0.0d, 0.0d, 0.0d);
		if (entitylivingbaseIn.isSaddled())
			this.getRenderer().render(
					this.getEntityModel().getModel(new Identifier(WoTRMod.MODID, "geo/drake.geo.json")),
					entitylivingbaseIn, partialTicks, cameo, matrixStackIn, bufferIn, bufferIn.getBuffer(cameo),
					packedLightIn, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
		matrixStackIn.pop();
	}
}
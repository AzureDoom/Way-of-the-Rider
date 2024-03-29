package mod.azure.wotr.client.render.entities.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class DrakeArmorLayer extends GeoRenderLayer<DrakeEntity> {

	public DrakeArmorLayer(GeoRenderer<DrakeEntity> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(PoseStack poseStack, DrakeEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		var armorRenderType = RenderType.armorCutoutNoCull(WoTRMod.modResource("textures/entity/layer/drake_armor_" + (animatable.getAttribute(Attributes.ARMOR).getValue() == 5 ? "iron" : animatable.getAttribute(Attributes.ARMOR).getValue() == 7 ? "gold" : "diamond") + ".png"));
		if (animatable.isWearingArmor())
			getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType, bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}
}
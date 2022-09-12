package mod.azure.wotr.client.render.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import mod.azure.wotr.client.models.items.DrakeSkullBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class DrakeSkullBlockRender extends GeoBlockRenderer<DrakeSkullEntity> {

	public DrakeSkullBlockRender() {
		super(new DrakeSkullBlockModel());
	}

	@Override
	public RenderType getRenderType(DrakeSkullEntity animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}

package mod.azure.wotr.client.render.items;

import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import mod.azure.wotr.client.models.items.DrakeSkullBlockModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class DrakeSkullBlockRender extends GeoBlockRenderer<DrakeSkullEntity> {

	public DrakeSkullBlockRender() {
		super(new DrakeSkullBlockModel());
	}

	@Override
	public RenderLayer getRenderType(DrakeSkullEntity animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
	}

}

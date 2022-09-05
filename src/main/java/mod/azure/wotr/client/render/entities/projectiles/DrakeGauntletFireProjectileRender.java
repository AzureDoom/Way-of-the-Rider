package mod.azure.wotr.client.render.entities.projectiles;

import mod.azure.wotr.client.models.entities.DrakeGauntletFireProjectileModel;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class DrakeGauntletFireProjectileRender extends GeoProjectilesRenderer<DrakeGauntletFireProjectile> {

	public DrakeGauntletFireProjectileRender(EntityRendererFactory.Context renderManagerIn) {
		super(renderManagerIn, new DrakeGauntletFireProjectileModel());
	}

	protected int getBlockLight(DrakeGauntletFireProjectile entityIn, BlockPos partialTicks) {
		return 15;
	}

	@Override
	public RenderLayer getRenderType(DrakeGauntletFireProjectile animatable, float partialTicks, MatrixStack stack,
			VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(getTextureResource(animatable));
	}

}
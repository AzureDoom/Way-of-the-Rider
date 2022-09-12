package mod.azure.wotr.client.render.entities.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.azure.wotr.client.models.entities.DrakeGauntletFireProjectileModel;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class DrakeGauntletFireProjectileRender extends GeoProjectilesRenderer<DrakeGauntletFireProjectile> {

	public DrakeGauntletFireProjectileRender(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn, new DrakeGauntletFireProjectileModel());
	}

	protected int getBlockLight(DrakeGauntletFireProjectile entityIn, BlockPos partialTicks) {
		return 15;
	}

	@Override
	public RenderType getRenderType(DrakeGauntletFireProjectile animatable, float partialTicks, PoseStack stack,
			MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
package mod.azure.wotr.client.render.entities.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.projectiles.mobs.DrakeFireProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class DrakeFireProjectiletRender extends EntityRenderer<DrakeFireProjectile> {

	public DrakeFireProjectiletRender(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getTextureLocation(DrakeFireProjectile entity) {
		return WoTRMod.modResource("textures/item/empty.png");
	}

	@Override
	public void render(DrakeFireProjectile persistentProjectileEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
		super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
		matrixStack.pushPose();
		matrixStack.scale(0, 0, 0);
		matrixStack.popPose();
	}

	@Override
	protected int getBlockLightLevel(DrakeFireProjectile entityIn, BlockPos partialTicks) {
		return 15;
	}

}
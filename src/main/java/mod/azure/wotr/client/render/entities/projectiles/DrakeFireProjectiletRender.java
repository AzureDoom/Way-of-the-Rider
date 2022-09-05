package mod.azure.wotr.client.render.entities.projectiles;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.entity.projectiles.mobs.DrakeFireProjectile;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class DrakeFireProjectiletRender extends EntityRenderer<DrakeFireProjectile> {

	public DrakeFireProjectiletRender(EntityRendererFactory.Context renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public Identifier getTexture(DrakeFireProjectile entity) {
		return new Identifier(WoTRMod.MODID, "textures/item/empty.png");
	}

	@Override
	public void render(DrakeFireProjectile persistentProjectileEntity, float f, float g, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
		matrixStack.push();
		matrixStack.scale(0, 0, 0);
		matrixStack.pop();
	}

	@Override
	protected int getBlockLight(DrakeFireProjectile entityIn, BlockPos partialTicks) {
		return 15;
	}

}
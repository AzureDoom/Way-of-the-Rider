package mod.azure.wotr.client.render.entities.projectiles;

import mod.azure.wotr.client.models.entities.DrakeGauntletFireProjectileModel;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrakeGauntletFireProjectileRender extends GeoEntityRenderer<DrakeGauntletFireProjectile> {

	public DrakeGauntletFireProjectileRender(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn, new DrakeGauntletFireProjectileModel());
	}

	protected int getBlockLight(DrakeGauntletFireProjectile entityIn, BlockPos partialTicks) {
		return 15;
	}

}
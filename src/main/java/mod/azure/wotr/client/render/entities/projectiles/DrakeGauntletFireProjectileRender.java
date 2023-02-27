package mod.azure.wotr.client.render.entities.projectiles;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.wotr.client.models.entities.DrakeGauntletFireProjectileModel;
import mod.azure.wotr.entity.projectiles.items.DrakeGauntletFireProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;

public class DrakeGauntletFireProjectileRender extends GeoEntityRenderer<DrakeGauntletFireProjectile> {

	public DrakeGauntletFireProjectileRender(EntityRendererProvider.Context renderManagerIn) {
		super(renderManagerIn, new DrakeGauntletFireProjectileModel());
	}

	protected int getBlockLight(DrakeGauntletFireProjectile entityIn, BlockPos partialTicks) {
		return 15;
	}

}
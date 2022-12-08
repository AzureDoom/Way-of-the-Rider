package mod.azure.wotr.client.render.items;

import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import mod.azure.wotr.client.models.items.DrakeSkullBlockModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DrakeSkullBlockRender extends GeoBlockRenderer<DrakeSkullEntity> {

	public DrakeSkullBlockRender() {
		super(new DrakeSkullBlockModel());
	}

}

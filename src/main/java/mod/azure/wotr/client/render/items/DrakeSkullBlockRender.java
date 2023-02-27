package mod.azure.wotr.client.render.items;

import mod.azure.azurelib.renderer.GeoBlockRenderer;
import mod.azure.wotr.blocks.tile.DrakeSkullEntity;
import mod.azure.wotr.client.models.items.DrakeSkullBlockModel;

public class DrakeSkullBlockRender extends GeoBlockRenderer<DrakeSkullEntity> {

	public DrakeSkullBlockRender() {
		super(new DrakeSkullBlockModel());
	}

}

package mod.azure.wotr.client.render.items;

import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.wotr.client.models.items.DrakeSkullItemModel;
import mod.azure.wotr.items.GeckoBlockItem;

public class DrakeSkullItemRender extends GeoItemRenderer<GeckoBlockItem> {

	public DrakeSkullItemRender() {
		super(new DrakeSkullItemModel());
	}
}
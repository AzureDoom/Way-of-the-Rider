package mod.azure.wotr.client.render.items;

import mod.azure.wotr.client.models.items.DrakeSkullItemModel;
import mod.azure.wotr.items.GeckoBlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DrakeSkullItemRender extends GeoItemRenderer<GeckoBlockItem> {

	public DrakeSkullItemRender() {
		super(new DrakeSkullItemModel());
	}
}
package mod.azure.wotr.client.render.items;

import mod.azure.wotr.client.models.items.DrakeSkullItemModel;
import mod.azure.wotr.items.GeckoBlockItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DrakeSkullItemRender extends GeoItemRenderer<GeckoBlockItem> {

	public DrakeSkullItemRender() {
		super(new DrakeSkullItemModel());
	}
}
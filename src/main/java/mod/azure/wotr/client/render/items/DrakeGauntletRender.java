package mod.azure.wotr.client.render.items;

import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.wotr.client.models.items.DrakeGauntletModel;
import mod.azure.wotr.items.DrakeGauntletItem;
import net.minecraft.world.item.ItemDisplayContext;

public class DrakeGauntletRender extends GeoItemRenderer<DrakeGauntletItem> {

	private ItemDisplayContext currentTransform;

	public DrakeGauntletRender() {
		super(new DrakeGauntletModel());
	}

	@Override
	public long getInstanceId(DrakeGauntletItem animatable) {
		if (currentTransform == ItemDisplayContext.GUI)
			return -1;
		return super.getInstanceId(animatable);
	}
}
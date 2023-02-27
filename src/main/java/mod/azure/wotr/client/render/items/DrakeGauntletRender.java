package mod.azure.wotr.client.render.items;

import mod.azure.azurelib.renderer.GeoItemRenderer;
import mod.azure.wotr.client.models.items.DrakeGauntletModel;
import mod.azure.wotr.items.DrakeGauntletItem;
import net.minecraft.client.renderer.block.model.ItemTransforms;

public class DrakeGauntletRender extends GeoItemRenderer<DrakeGauntletItem> {

	private ItemTransforms.TransformType currentTransform;

	public DrakeGauntletRender() {
		super(new DrakeGauntletModel());
	}
	
	@Override
	public long getInstanceId(DrakeGauntletItem animatable) {
		if (currentTransform == ItemTransforms.TransformType.GUI) {
			return -1;
		}
		return super.getInstanceId(animatable);
	}
}
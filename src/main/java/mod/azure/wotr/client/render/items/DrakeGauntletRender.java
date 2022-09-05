package mod.azure.wotr.client.render.items;

import mod.azure.wotr.client.models.items.DrakeGauntletModel;
import mod.azure.wotr.items.DrakeGauntletItem;
import net.minecraft.client.render.model.json.ModelTransformation;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DrakeGauntletRender extends GeoItemRenderer<DrakeGauntletItem> {

	private ModelTransformation.Mode currentTransform;

	public DrakeGauntletRender() {
		super(new DrakeGauntletModel());
	}

	@Override
	public Integer getUniqueID(DrakeGauntletItem animatable) {
		if (currentTransform == ModelTransformation.Mode.GUI) {
			return -1;
		}
		return super.getUniqueID(animatable);
	}
}
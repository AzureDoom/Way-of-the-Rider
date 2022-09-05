package mod.azure.wotr.items;

import mod.azure.wotr.WoTRMod;
import net.minecraft.item.Item;

public class DrakeArmorItem extends Item {
	private final int bonus;

	public DrakeArmorItem(int bonus) {
		super(new Item.Settings().maxCount(1).group(WoTRMod.WoTRItemGroup));
		this.bonus = bonus;
	}

	public int getBonus() {
		return this.bonus;
	}
}

package mod.azure.wotr.items;

import net.minecraft.world.item.Item;

public class DrakeArmorItem extends Item {
    private final int bonus;

    public DrakeArmorItem(int bonus) {
        super(new Item.Properties().stacksTo(1));
        this.bonus = bonus;
    }

    public int getBonus() {
        return this.bonus;
    }
}

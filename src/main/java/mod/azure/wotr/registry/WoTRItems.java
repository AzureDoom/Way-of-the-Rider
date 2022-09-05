package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.DrakeArmorItem;
import mod.azure.wotr.items.DrakeGauntletItem;
import mod.azure.wotr.items.GeckoBlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WoTRItems {

	public static DrakeGauntletItem DRAKE_GAUNTLET = Registry.register(Registry.ITEM,
			new Identifier(WoTRMod.MODID, "drake_gauntlet"), new DrakeGauntletItem());

	public static DrakeArmorItem DRAKE_ARMOR_IRON = Registry.register(Registry.ITEM,
			new Identifier(WoTRMod.MODID, "drake_armor_iron"), new DrakeArmorItem(5));

	public static DrakeArmorItem DRAKE_ARMOR_GOLD = Registry.register(Registry.ITEM,
			new Identifier(WoTRMod.MODID, "drake_armor_gold"), new DrakeArmorItem(7));

	public static DrakeArmorItem DRAKE_ARMOR_DIAMOND = Registry.register(Registry.ITEM,
			new Identifier(WoTRMod.MODID, "drake_armor_diamond"), new DrakeArmorItem(11));

	public static GeckoBlockItem DRAKE_SKULL = Registry.register(Registry.ITEM,
			new Identifier(WoTRMod.MODID, "drake_skull"),
			new GeckoBlockItem(WoTRBlocks.DRAKE_SKULL, new Item.Settings().fireproof().group(WoTRMod.WoTRItemGroup)));
}

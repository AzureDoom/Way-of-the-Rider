package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.DrakeArmorItem;
import mod.azure.wotr.items.DrakeGauntletItem;
import mod.azure.wotr.items.GeckoBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WoTRItems {

	public static DrakeGauntletItem DRAKE_GAUNTLET = Registry.register(Registry.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_gauntlet"), new DrakeGauntletItem());

	public static DrakeArmorItem DRAKE_ARMOR_IRON = Registry.register(Registry.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_armor_iron"), new DrakeArmorItem(5));

	public static DrakeArmorItem DRAKE_ARMOR_GOLD = Registry.register(Registry.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_armor_gold"), new DrakeArmorItem(7));

	public static DrakeArmorItem DRAKE_ARMOR_DIAMOND = Registry.register(Registry.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_armor_diamond"), new DrakeArmorItem(11));

	public static GeckoBlockItem DRAKE_SKULL = Registry.register(Registry.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_skull"),
			new GeckoBlockItem(WoTRBlocks.DRAKE_SKULL, new Item.Properties().fireResistant().tab(WoTRMod.WoTRItemGroup)));
}

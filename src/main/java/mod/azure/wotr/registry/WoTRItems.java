package mod.azure.wotr.registry;

import mod.azure.azurelib.items.AzureSpawnEgg;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.DrakeArmorItem;
import mod.azure.wotr.items.DrakeGauntletItem;
import mod.azure.wotr.items.GeckoBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class WoTRItems {

	public static DrakeGauntletItem DRAKE_GAUNTLET = Registry.register(BuiltInRegistries.ITEM, WoTRMod.modResource("drake_gauntlet"), new DrakeGauntletItem());

	public static DrakeArmorItem DRAKE_ARMOR_IRON = Registry.register(BuiltInRegistries.ITEM, WoTRMod.modResource("drake_armor_iron"), new DrakeArmorItem(5));

	public static DrakeArmorItem DRAKE_ARMOR_GOLD = Registry.register(BuiltInRegistries.ITEM, WoTRMod.modResource("drake_armor_gold"), new DrakeArmorItem(7));

	public static DrakeArmorItem DRAKE_ARMOR_DIAMOND = Registry.register(BuiltInRegistries.ITEM, WoTRMod.modResource("drake_armor_diamond"), new DrakeArmorItem(11));

	public static GeckoBlockItem DRAKE_SKULL = Registry.register(BuiltInRegistries.ITEM, WoTRMod.modResource("drake_skull"), new GeckoBlockItem(WoTRBlocks.DRAKE_SKULL, new Item.Properties().fireResistant()));

	public static AzureSpawnEgg DRAKE_SPAWN_EGG = Registry.register(BuiltInRegistries.ITEM, WoTRMod.modResource("drake_spawn_egg"), new AzureSpawnEgg(WoTREntities.DRAKE, 0x58252d, 0x352728));

	public static AzureSpawnEgg LUNG_SERPENT_SPAWN_EGG = Registry.register(BuiltInRegistries.ITEM, WoTRMod.modResource("lung_serpent_spawn_egg"), new AzureSpawnEgg(WoTREntities.LUNG_SERPENT, 0xefe7c8, 0x833e28));

}

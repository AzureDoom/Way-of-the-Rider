package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.items.DrakeArmorItem;
import mod.azure.wotr.items.DrakeGauntletItem;
import mod.azure.wotr.items.GeckoBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import software.bernie.example.registry.EntityRegistry;

public class WoTRItems {

	public static DrakeGauntletItem DRAKE_GAUNTLET = Registry.register(BuiltInRegistries.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_gauntlet"), new DrakeGauntletItem());

	public static DrakeArmorItem DRAKE_ARMOR_IRON = Registry.register(BuiltInRegistries.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_armor_iron"), new DrakeArmorItem(5));

	public static DrakeArmorItem DRAKE_ARMOR_GOLD = Registry.register(BuiltInRegistries.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_armor_gold"), new DrakeArmorItem(7));

	public static DrakeArmorItem DRAKE_ARMOR_DIAMOND = Registry.register(BuiltInRegistries.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_armor_diamond"), new DrakeArmorItem(11));

	public static GeckoBlockItem DRAKE_SKULL = Registry.register(BuiltInRegistries.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_skull"),
			new GeckoBlockItem(WoTRBlocks.DRAKE_SKULL, new Item.Properties().fireResistant()));

	public static SpawnEggItem DRAKE_SPAWN_EGG = Registry.register(BuiltInRegistries.ITEM,
			new ResourceLocation(WoTRMod.MODID, "drake_spawn_egg"),
			new SpawnEggItem(EntityRegistry.BAT, 0x58252d, 0x352728, new Item.Properties()));

}

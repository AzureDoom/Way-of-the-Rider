package mod.azure.wotr;

import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.AzureLibMod;
import mod.azure.azurelib.config.format.ConfigFormats;
import mod.azure.wotr.config.WoTRConfig;
import mod.azure.wotr.entity.DrakeEntity;
import mod.azure.wotr.entity.LungSerpentEntity;
import mod.azure.wotr.items.DrakeGauntletItem;
import mod.azure.wotr.registry.WoTRBlocks;
import mod.azure.wotr.registry.WoTREntities;
import mod.azure.wotr.registry.WoTRItems;
import mod.azure.wotr.registry.WoTRSounds;
import mod.azure.wotr.registry.WoTRStructures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class WoTRMod implements ModInitializer {

	public static WoTRConfig config;
	public static WoTRSounds SOUNDS;
	public static WoTRItems ITEMS;
	public static WoTRBlocks BLOCKS;
	public static WoTREntities ENTITIES;
	public static final String MODID = "wotr";
	public static final ResourceLocation RELOAD = new ResourceLocation(MODID, "reload");
	public static final ResourceKey<CreativeModeTab> GENERAL = ResourceKey.create(Registries.CREATIVE_MODE_TAB, modResource("wotr"));

	public static final ResourceLocation modResource(String name) {
		return new ResourceLocation(MODID, name);
	}

	@Override
	public void onInitialize() {
		config = AzureLibMod.registerConfig(WoTRConfig.class, ConfigFormats.json()).getConfigInstance();
		ITEMS = new WoTRItems();
		BLOCKS = new WoTRBlocks();
		SOUNDS = new WoTRSounds();
		ENTITIES = new WoTREntities();
		WoTRStructures.registerStructureFeatures();
		FabricDefaultAttributeRegistry.register(WoTREntities.DRAKE, DrakeEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(WoTREntities.LUNG_SERPENT, LungSerpentEntity.createMobAttributes());
		ServerPlayNetworking.registerGlobalReceiver(WoTRMod.RELOAD, (server, player, serverPlayNetworkHandler, inputPacket, packetSender) -> {
			if (player.getMainHandItem().getItem() instanceof DrakeGauntletItem)
				((DrakeGauntletItem) player.getMainHandItem().getItem()).reload(player, InteractionHand.MAIN_HAND);
		});
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, GENERAL, FabricItemGroup.builder().icon(() -> new ItemStack(WoTRItems.DRAKE_SKULL)) // icon
				.title(Component.translatable("itemGroup.wotr.wotr")) // title
				.displayItems((context, entries) -> {
					entries.accept(WoTRItems.DRAKE_SKULL);
					entries.accept(WoTRItems.DRAKE_GAUNTLET);
					entries.accept(WoTRItems.DRAKE_ARMOR_IRON);
					entries.accept(WoTRItems.DRAKE_ARMOR_GOLD);
					entries.accept(WoTRItems.DRAKE_ARMOR_DIAMOND);
					entries.accept(WoTRItems.DRAKE_SPAWN_EGG);
					entries.accept(WoTRItems.LUNG_SERPENT_SPAWN_EGG);
				}).build());
	}
}

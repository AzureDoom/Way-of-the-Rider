package mod.azure.wotr;

import mod.azure.wotr.config.CustomMidnightConfig;
import mod.azure.wotr.config.WoTRConfig;
import mod.azure.wotr.entity.DrakeEntity;
import mod.azure.wotr.items.DrakeGauntletItem;
import mod.azure.wotr.registry.WoTRBlocks;
import mod.azure.wotr.registry.WoTREntities;
import mod.azure.wotr.registry.WoTRItems;
import mod.azure.wotr.registry.WoTRSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class WoTRMod implements ModInitializer {

	public static WoTRSounds SOUNDS;
	public static WoTRItems ITEMS;
	public static WoTRBlocks BLOCKS;
	public static WoTREntities ENTITIES;
	public static final String MODID = "wotr";
	public static final Identifier RELOAD = new Identifier(MODID, "reload");
	public static final ItemGroup WoTRItemGroup = FabricItemGroupBuilder.create(new Identifier(MODID, "wotr"))
			.icon(() -> new ItemStack(WoTRItems.DRAKE_SKULL)).build();

	@Override
	public void onInitialize() {
		CustomMidnightConfig.init(MODID, WoTRConfig.class);
		ITEMS = new WoTRItems();
		BLOCKS = new WoTRBlocks();
		SOUNDS = new WoTRSounds();
		ENTITIES = new WoTREntities();
		FabricDefaultAttributeRegistry.register(WoTREntities.DRAKE, DrakeEntity.createMobAttributes());
		ServerPlayNetworking.registerGlobalReceiver(WoTRMod.RELOAD,
				(server, player, serverPlayNetworkHandler, inputPacket, packetSender) -> {
					if (player.getMainHandStack().getItem() instanceof DrakeGauntletItem) {
						((DrakeGauntletItem) player.getMainHandStack().getItem()).reload(player, Hand.MAIN_HAND);
					}
					;
				});
	}
}

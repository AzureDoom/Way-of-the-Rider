package mod.azure.wotr.client;

import java.util.UUID;

import org.lwjgl.glfw.GLFW;

import io.netty.buffer.Unpooled;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.client.render.entities.DrakeRender;
import mod.azure.wotr.client.render.entities.projectiles.DrakeFireProjectiletRender;
import mod.azure.wotr.client.render.entities.projectiles.DrakeGauntletFireProjectileRender;
import mod.azure.wotr.client.render.items.DrakeGauntletRender;
import mod.azure.wotr.client.render.items.DrakeSkullBlockRender;
import mod.azure.wotr.client.render.items.DrakeSkullItemRender;
import mod.azure.wotr.registry.WoTRBlocks;
import mod.azure.wotr.registry.WoTREntities;
import mod.azure.wotr.registry.WoTRItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class WoTRClientMod implements ClientModInitializer {

	public static KeyBinding reload = new KeyBinding("key." + WoTRMod.MODID + ".reload", InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_R, "category." + WoTRMod.MODID + ".binds");

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(reload);
		GeoItemRenderer.registerItemRenderer(WoTRItems.DRAKE_GAUNTLET, new DrakeGauntletRender());
		EntityRendererRegistry.register(WoTREntities.DRAKE, (ctx) -> new DrakeRender(ctx));
		EntityRendererRegistry.register(WoTREntities.DRAKE_FIRE, (ctx) -> new DrakeFireProjectiletRender(ctx));
		GeoItemRenderer.registerItemRenderer(WoTRBlocks.DRAKE_SKULL.asItem(), new DrakeSkullItemRender());
		BlockRenderLayerMap.INSTANCE.putBlock(WoTRBlocks.DRAKE_SKULL, RenderLayer.getTranslucent());
		BlockEntityRendererRegistry.register(WoTREntities.DRAKE_SKULL,
				(BlockEntityRendererFactory.Context rendererDispatcherIn) -> new DrakeSkullBlockRender());
		EntityRendererRegistry.register(WoTREntities.DRAKE_GAUNTLET_FIRE,
				(ctx) -> new DrakeGauntletFireProjectileRender(ctx));
		ClientPlayNetworking.registerGlobalReceiver(EntityPacket.ID, (client, handler, buf, responseSender) -> {
			EntityPacketOnClient.onPacket(client, buf);
		});
	}

	public class EntityPacketOnClient {
		@Environment(EnvType.CLIENT)
		public static void onPacket(MinecraftClient context, PacketByteBuf byteBuf) {
			EntityType<?> type = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
			UUID entityUUID = byteBuf.readUuid();
			int entityID = byteBuf.readVarInt();
			double x = byteBuf.readDouble();
			double y = byteBuf.readDouble();
			double z = byteBuf.readDouble();
			float pitch = (byteBuf.readByte() * 360) / 256.0F;
			float yaw = (byteBuf.readByte() * 360) / 256.0F;
			context.execute(() -> {
				ClientWorld world = MinecraftClient.getInstance().world;
				Entity entity = type.create(world);
				if (entity != null) {
					entity.updatePosition(x, y, z);
					entity.updateTrackedPosition(x, y, z);
					entity.setPitch(pitch);
					entity.setYaw(yaw);
					entity.setId(entityID);
					entity.setUuid(entityUUID);
					world.addEntity(entityID, entity);
				}
			});
		}
	}

	public class EntityPacket {
		public static final Identifier ID = new Identifier(WoTRMod.MODID, "spawn_entity");

		public static Packet<?> createPacket(Entity entity) {
			PacketByteBuf buf = createBuffer();
			buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(entity.getType()));
			buf.writeUuid(entity.getUuid());
			buf.writeVarInt(entity.getId());
			buf.writeDouble(entity.getX());
			buf.writeDouble(entity.getY());
			buf.writeDouble(entity.getZ());
			buf.writeByte(MathHelper.floor(entity.getPitch() * 256.0F / 360.0F));
			buf.writeByte(MathHelper.floor(entity.getYaw() * 256.0F / 360.0F));
			buf.writeFloat(entity.getPitch());
			buf.writeFloat(entity.getYaw());
			return ServerPlayNetworking.createS2CPacket(ID, buf);
		}

		private static PacketByteBuf createBuffer() {
			return new PacketByteBuf(Unpooled.buffer());
		}

	}

}

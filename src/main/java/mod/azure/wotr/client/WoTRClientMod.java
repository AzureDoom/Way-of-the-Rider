package mod.azure.wotr.client;

import java.util.UUID;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import io.netty.buffer.Unpooled;
import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.client.render.entities.DrakeRender;
import mod.azure.wotr.client.render.entities.projectiles.DrakeFireProjectiletRender;
import mod.azure.wotr.client.render.entities.projectiles.DrakeGauntletFireProjectileRender;
import mod.azure.wotr.client.render.items.DrakeSkullBlockRender;
import mod.azure.wotr.registry.WoTRBlocks;
import mod.azure.wotr.registry.WoTREntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class WoTRClientMod implements ClientModInitializer {

	public static KeyMapping reload = new KeyMapping("key." + WoTRMod.MODID + ".reload", InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_R, "category." + WoTRMod.MODID + ".binds");

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(reload);
		EntityRendererRegistry.register(WoTREntities.DRAKE, (ctx) -> new DrakeRender(ctx));
		EntityRendererRegistry.register(WoTREntities.DRAKE_FIRE, (ctx) -> new DrakeFireProjectiletRender(ctx));
		BlockRenderLayerMap.INSTANCE.putBlock(WoTRBlocks.DRAKE_SKULL, RenderType.translucent());
		BlockEntityRendererRegistry.register(WoTREntities.DRAKE_SKULL,
				(BlockEntityRendererProvider.Context rendererDispatcherIn) -> new DrakeSkullBlockRender());
		EntityRendererRegistry.register(WoTREntities.DRAKE_GAUNTLET_FIRE,
				(ctx) -> new DrakeGauntletFireProjectileRender(ctx));
		ClientPlayNetworking.registerGlobalReceiver(EntityPacket.ID, (client, handler, buf, responseSender) -> {
			EntityPacketOnClient.onPacket(client, buf);
		});
	}

	public class EntityPacketOnClient {
		@Environment(EnvType.CLIENT)
		public static void onPacket(Minecraft context, FriendlyByteBuf byteBuf) {
			EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.byId(byteBuf.readVarInt());
			UUID entityUUID = byteBuf.readUUID();
			int entityID = byteBuf.readVarInt();
			double x = byteBuf.readDouble();
			double y = byteBuf.readDouble();
			double z = byteBuf.readDouble();
			float pitch = (byteBuf.readByte() * 360) / 256.0F;
			float yaw = (byteBuf.readByte() * 360) / 256.0F;
			context.execute(() -> {
				ClientLevel world = Minecraft.getInstance().level;
				Entity entity = type.create(world);
				if (entity != null) {
					entity.absMoveTo(x, y, z);
					entity.syncPacketPositionCodec(x, y, z);
					entity.setXRot(pitch);
					entity.setYRot(yaw);
					entity.setId(entityID);
					entity.setUUID(entityUUID);
					world.putNonPlayerEntity(entityID, entity);
				}
			});
		}
	}

	public class EntityPacket {
		public static final ResourceLocation ID = new ResourceLocation(WoTRMod.MODID, "spawn_entity");

		public static Packet<?> createPacket(Entity entity) {
			FriendlyByteBuf buf = createBuffer();
			buf.writeVarInt(BuiltInRegistries.ENTITY_TYPE.getId(entity.getType()));
			buf.writeUUID(entity.getUUID());
			buf.writeVarInt(entity.getId());
			buf.writeDouble(entity.getX());
			buf.writeDouble(entity.getY());
			buf.writeDouble(entity.getZ());
			buf.writeByte(Mth.floor(entity.getXRot() * 256.0F / 360.0F));
			buf.writeByte(Mth.floor(entity.getYRot() * 256.0F / 360.0F));
			buf.writeFloat(entity.getXRot());
			buf.writeFloat(entity.getYRot());
			return ServerPlayNetworking.createS2CPacket(ID, buf);
		}

		private static FriendlyByteBuf createBuffer() {
			return new FriendlyByteBuf(Unpooled.buffer());
		}

	}

}

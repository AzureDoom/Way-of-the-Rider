package mod.azure.wotr.client;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.client.render.entities.DrakeRender;
import mod.azure.wotr.client.render.entities.LungSerpentRender;
import mod.azure.wotr.client.render.entities.projectiles.DrakeFireProjectiletRender;
import mod.azure.wotr.client.render.entities.projectiles.DrakeGauntletFireProjectileRender;
import mod.azure.wotr.client.render.items.DrakeSkullBlockRender;
import mod.azure.wotr.registry.WoTRBlocks;
import mod.azure.wotr.registry.WoTREntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class WoTRClientMod implements ClientModInitializer {

	public static KeyMapping reload = new KeyMapping("key." + WoTRMod.MODID + ".reload", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "category." + WoTRMod.MODID + ".binds");

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(reload);
		EntityRendererRegistry.register(WoTREntities.DRAKE, (ctx) -> new DrakeRender(ctx));
		EntityRendererRegistry.register(WoTREntities.LUNG_SERPENT, (ctx) -> new LungSerpentRender(ctx));
		EntityRendererRegistry.register(WoTREntities.DRAKE_FIRE, (ctx) -> new DrakeFireProjectiletRender(ctx));
		BlockRenderLayerMap.INSTANCE.putBlock(WoTRBlocks.DRAKE_SKULL, RenderType.translucent());
		BlockEntityRendererRegistry.register(WoTREntities.DRAKE_SKULL, (BlockEntityRendererProvider.Context rendererDispatcherIn) -> new DrakeSkullBlockRender());
		EntityRendererRegistry.register(WoTREntities.DRAKE_GAUNTLET_FIRE, (ctx) -> new DrakeGauntletFireProjectileRender(ctx));
	}

}

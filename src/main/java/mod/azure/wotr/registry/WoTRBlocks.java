package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.DrakeSkullBlock;
import mod.azure.wotr.blocks.TickingLightBlock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class WoTRBlocks {

	public static final TickingLightBlock TICKING_LIGHT_BLOCK = Registry.register(Registry.BLOCK,
			new ResourceLocation(WoTRMod.MODID, "lightblock"), new TickingLightBlock());

	public static final DrakeSkullBlock DRAKE_SKULL = Registry.register(Registry.BLOCK,
			new ResourceLocation(WoTRMod.MODID, "drake_skull"), new DrakeSkullBlock());

}

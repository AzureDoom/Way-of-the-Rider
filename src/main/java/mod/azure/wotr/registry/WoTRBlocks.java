package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.DrakeSkullBlock;
import mod.azure.wotr.blocks.TickingLightBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WoTRBlocks {

	public static final TickingLightBlock TICKING_LIGHT_BLOCK = Registry.register(Registry.BLOCK,
			new Identifier(WoTRMod.MODID, "lightblock"), new TickingLightBlock());

	public static final DrakeSkullBlock DRAKE_SKULL = Registry.register(Registry.BLOCK,
			new Identifier(WoTRMod.MODID, "drake_skull"), new DrakeSkullBlock());

}

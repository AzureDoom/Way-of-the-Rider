package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.DrakeSkullBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class WoTRBlocks {

	public static final DrakeSkullBlock DRAKE_SKULL = Registry.register(BuiltInRegistries.BLOCK,
			new ResourceLocation(WoTRMod.MODID, "drake_skull"), new DrakeSkullBlock());

}

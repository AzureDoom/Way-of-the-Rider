package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.blocks.DrakeSkullBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class WoTRBlocks {

    public static final DrakeSkullBlock DRAKE_SKULL = Registry.register(BuiltInRegistries.BLOCK, WoTRMod.modResource("drake_skull"), new DrakeSkullBlock());

}

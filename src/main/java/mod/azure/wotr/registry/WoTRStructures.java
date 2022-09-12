package mod.azure.wotr.registry;

import com.mojang.serialization.Codec;

import mod.azure.wotr.WoTRMod;
import mod.azure.wotr.structures.DragonCaveStructure;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class WoTRStructures {
	public static StructureType<?> DRAGON_CAVE;

	public static void registerStructureFeatures() {
		DRAGON_CAVE = register(new ResourceLocation(WoTRMod.MODID, "dragon_cave"), DragonCaveStructure.CODEC);
	}

	private static <S extends Structure> StructureType<S> register(ResourceLocation id, Codec<S> codec) {
		return Registry.register(Registry.STRUCTURE_TYPES, id, () -> codec);
	}

}

package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class WoTRSounds {

	public static SoundEvent DRAKE_IDLE = Registry.register(BuiltInRegistries.SOUND_EVENT,
			new ResourceLocation(WoTRMod.MODID, "drake_idle"),
			SoundEvent.createVariableRangeEvent(new ResourceLocation(WoTRMod.MODID, "drake_idle")));
}

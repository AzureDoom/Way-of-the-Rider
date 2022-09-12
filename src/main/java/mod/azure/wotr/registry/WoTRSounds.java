package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class WoTRSounds {

	public static SoundEvent DRAKE_IDLE = Registry.register(Registry.SOUND_EVENT,
			new ResourceLocation(WoTRMod.MODID, "drake_idle"),
			new SoundEvent(new ResourceLocation(WoTRMod.MODID, "drake_idle")));
}

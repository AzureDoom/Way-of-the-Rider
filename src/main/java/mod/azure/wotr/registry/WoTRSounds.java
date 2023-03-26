package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class WoTRSounds {

	public static SoundEvent DRAKE_IDLE = Registry.register(BuiltInRegistries.SOUND_EVENT, WoTRMod.modResource("drake_idle"), SoundEvent.createVariableRangeEvent(WoTRMod.modResource("drake_idle")));
}

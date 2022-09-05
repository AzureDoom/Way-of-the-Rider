package mod.azure.wotr.registry;

import mod.azure.wotr.WoTRMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WoTRSounds {

	public static SoundEvent DRAKE_IDLE = Registry.register(Registry.SOUND_EVENT,
			new Identifier(WoTRMod.MODID, "drake_idle"), new SoundEvent(new Identifier(WoTRMod.MODID, "drake_idle")));
}

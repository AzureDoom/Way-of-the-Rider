package mod.azure.wotr.mixin;

import org.spongepowered.asm.mixin.Mixin;

import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class PlayerMixin {

	public static void openDrakeInventory(DrakeEntity horse, Inventory inventory) {
	}
}

package mod.azure.wotr.mixin;

import org.spongepowered.asm.mixin.Mixin;

import mod.azure.wotr.entity.DrakeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin {

	public static void openDrakeInventory(DrakeEntity horse, Inventory inventory) {
	}
}

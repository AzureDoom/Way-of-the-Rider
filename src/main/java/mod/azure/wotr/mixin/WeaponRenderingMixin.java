package mod.azure.wotr.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.azure.wotr.items.DrakeGauntletItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Mixin(PlayerRenderer.class)
public class WeaponRenderingMixin {

	@Inject(method = "getArmPose", at = @At(value = "TAIL"), cancellable = true)
	private static void tryItemPose(AbstractClientPlayer player, InteractionHand hand,
			CallbackInfoReturnable< HumanoidModel.ArmPose> ci) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() instanceof DrakeGauntletItem) {
			ci.setReturnValue( HumanoidModel.ArmPose.BOW_AND_ARROW);
		}
	}
}

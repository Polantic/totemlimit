package com.polantic.totemlimit.mixin;
import com.polantic.totemlimit.Totemlimit;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getStack();
    @Inject(method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V",
            at = @At("HEAD"), cancellable = true)
    private void totemlimit$blockSecondTotemPickup(PlayerEntity player, CallbackInfo ci) {
        if (!(player instanceof ServerPlayerEntity sp)) return;
        ItemStack stack = this.getStack();
        if (!Totemlimit.isTotem(stack)) return;
        // If they already have a totem (inventory/offhand), stop pickup
        if (Totemlimit.hasAnyTotem(sp, false)) {
            Totemlimit.warn(sp);
            ci.cancel();
        }
    }
}
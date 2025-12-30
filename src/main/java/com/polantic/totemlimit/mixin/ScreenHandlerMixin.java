package com.polantic.totemlimit.mixin;
import com.polantic.totemlimit.Totemlimit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow
    public DefaultedList<Slot> slots;

    @Inject(method = "onSlotClick(IILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V",
            at = @At("HEAD"), cancellable = true)
    private void totemlimit$blockSecondTotemFromContainers(
            int slotIndex, int button, SlotActionType actionType,
            PlayerEntity player, CallbackInfo ci
    ) {
        if (!(player instanceof ServerPlayerEntity sp)) return;
        if (slotIndex < 0 || slotIndex >= this.slots.size()) return;
        Slot slot = this.slots.get(slotIndex);
        ItemStack clicked = slot.getStack();
        if (!Totemlimit.isTotem(clicked)) return;
        // Let moving totems around inside player inventory
        Inventory slotInv = slot.inventory;
        boolean isPlayerInventorySlot = (slotInv == sp.getInventory());
        if (isPlayerInventorySlot) return;
        // Stop taking a totem from a container if player already has one elsewhere
        if (Totemlimit.hasTotemExcluding(sp, clicked, false)) {
            Totemlimit.warn(sp);
            ci.cancel();
        }
    }
}
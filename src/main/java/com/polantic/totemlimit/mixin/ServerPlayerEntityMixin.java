package com.polantic.totemlimit.mixin;

import com.polantic.totemlimit.Totemlimit;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void totemlimit$enforceSingleTotem(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        Totemlimit.enforceSingleTotem(player);
    }
}
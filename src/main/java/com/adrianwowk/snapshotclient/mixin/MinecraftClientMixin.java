package com.adrianwowk.snapshotclient.mixin;

import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    public ClientPlayerEntity player;
    @Shadow
    public GameOptions options;

    @Inject(method = "hasOutline", at = @At(value = "HEAD"), cancellable = true)
    private void inject(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if (Mods.HIGHLIGHT_PLAYER_MOD.enabled) {
            cir.setReturnValue(this.player != null && entity.getType() == EntityType.PLAYER);
        } else {
            cir.setReturnValue(entity.isGlowing() || this.player != null && this.player.isSpectator() && this.options.keySpectatorOutlines.isPressed() && entity.getType() == EntityType.PLAYER);
        }
    }
}

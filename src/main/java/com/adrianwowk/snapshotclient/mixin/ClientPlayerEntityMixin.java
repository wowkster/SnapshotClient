package com.adrianwowk.snapshotclient.mixin;

import com.adrianwowk.snapshotclient.client.mods.Mods;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Shadow
    private int underwaterVisibilityTicks;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "getUnderwaterVisibility", at = @At("HEAD"), cancellable = true)
    public void inject(CallbackInfoReturnable<Float> cir){
        if (Mods.CLEAR_WATER_MOD.enabled){
            cir.setReturnValue(3.0F);
        } else {
            if (!this.isSubmergedIn(FluidTags.WATER)) {
                cir.setReturnValue(0.0F);
            } else {
                if ((float)this.underwaterVisibilityTicks >= 600.0F) {
                    cir.setReturnValue(1.0F);
                } else {
                    float h = MathHelper.clamp((float)this.underwaterVisibilityTicks / 100.0F, 0.0F, 1.0F);
                    float i = (float)this.underwaterVisibilityTicks < 100.0F ? 0.0F : MathHelper.clamp(((float)this.underwaterVisibilityTicks - 100.0F) / 500.0F, 0.0F, 1.0F);
                    cir.setReturnValue(h * 0.6F + i * 0.39999998F);
                }
            }
        }
    }
}

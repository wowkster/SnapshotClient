package com.adrianwowk.snapshotclient.mixin;

import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    private LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "hasLabel", at = @At("HEAD"), cancellable = true)
    protected void inject(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
        double d = this.dispatcher.getSquaredDistanceToCamera(livingEntity);
        float f = livingEntity.isSneaky() ? 32.0F : 64.0F;
        if (d >= (double)(f * f)) {
            cir.setReturnValue(false);
        } else {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
            boolean bl = !livingEntity.isInvisibleTo(clientPlayerEntity);
            if (livingEntity != clientPlayerEntity || Mods.NAME_TAG_MOD.enabled ) {
                AbstractTeam abstractTeam = livingEntity.getScoreboardTeam();
                AbstractTeam abstractTeam2 = clientPlayerEntity.getScoreboardTeam();
                if (abstractTeam != null) {
                    AbstractTeam.VisibilityRule visibilityRule = abstractTeam.getNameTagVisibilityRule();
                    switch(visibilityRule) {
                        case ALWAYS:
                            cir.setReturnValue(bl);
                            return;
                        case NEVER:
                            cir.setReturnValue(false);
                            return;
                        case HIDE_FOR_OTHER_TEAMS:
                            cir.setReturnValue(abstractTeam2 == null ? bl : abstractTeam.isEqual(abstractTeam2) && (abstractTeam.shouldShowFriendlyInvisibles() || bl));
                            return;
                        case HIDE_FOR_OWN_TEAM:
                            cir.setReturnValue(abstractTeam2 == null ? bl : !abstractTeam.isEqual(abstractTeam2) && bl);
                            return;
                        default:
                            cir.setReturnValue(true);
                            return;
                    }
                }
            }
            cir.setReturnValue(MinecraftClient.isHudEnabled() && livingEntity != minecraftClient.getCameraEntity() && bl && !livingEntity.hasPassengers());
        }
    }

    @Shadow
    public M getModel() {
        return null;
    }

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }
}

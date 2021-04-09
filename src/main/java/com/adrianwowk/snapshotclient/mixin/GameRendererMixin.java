package com.adrianwowk.snapshotclient.mixin;

import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin{
    @Shadow
    private boolean renderingPanorama;
    @Shadow
    private MinecraftClient client;
    @Shadow
    private LightmapTextureManager lightmapTextureManager;
    @Shadow
    private HeldItemRenderer firstPersonRenderer;
    @Shadow
    private BufferBuilderStorage buffers;

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    public void inject(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo info){
        if (Mods.ZOOM_MOD.enabled){
            info.cancel();
            return;
        }
        if (!this.renderingPanorama) {
            this.loadProjectionMatrix(this.getBasicProjectionMatrix(this.getFov(camera, tickDelta, false)));
            MatrixStack.Entry entry = matrices.peek();
            entry.getModel().loadIdentity();
            entry.getNormal().loadIdentity();
            matrices.push();
            this.bobViewWhenHurt(matrices, tickDelta);
            if (this.client.options.bobView) {
                this.bobView(matrices, tickDelta);
            }

            boolean bl = this.client.getCameraEntity() instanceof LivingEntity && ((LivingEntity)this.client.getCameraEntity()).isSleeping();
            if (this.client.options.getPerspective().isFirstPerson() && !bl && !this.client.options.hudHidden && this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
                this.lightmapTextureManager.enable();
                this.firstPersonRenderer.renderItem(tickDelta, matrices, this.buffers.getEntityVertexConsumers(), this.client.player, this.client.getEntityRenderDispatcher().getLight(this.client.player, tickDelta));
                this.lightmapTextureManager.disable();
            }

            matrices.pop();
            if (this.client.options.getPerspective().isFirstPerson() && !bl) {
                InGameOverlayRenderer.renderOverlays(this.client, matrices);
                this.bobViewWhenHurt(matrices, tickDelta);
            }

            if (this.client.options.bobView) {
                this.bobView(matrices, tickDelta);
            }
        }
        info.cancel();
    }

    @Shadow
    private void loadProjectionMatrix(Matrix4f basicProjectionMatrix) {
    }

    @Shadow
    private void bobView(MatrixStack matrices, float tickDelta) {
    }

    @Shadow
    private void bobViewWhenHurt(MatrixStack matrices, float tickDelta) {
    }

    @Shadow
    private Matrix4f getBasicProjectionMatrix(double fov) {
        return null;
    }

    @Shadow
    private double getFov(Camera camera, float tickDelta, boolean b) {
        return 0;
    }
}

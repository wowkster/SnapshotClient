package com.adrianwowk.snapshotclient.mixin;


import com.adrianwowk.snapshotclient.client.mods.Mods;
import com.adrianwowk.snapshotclient.client.mods.key.PerspectiveMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.SmoothUtil;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Mouse.class })
public class MouseMixin
{
    @Shadow
    private MinecraftClient client;
    @Shadow
    private double cursorDeltaX;
    @Shadow
    private double cursorDeltaY;
    @Shadow
    private double lastMouseUpdateTime;
    @Shadow
    private boolean cursorLocked;
    @Shadow
    private final SmoothUtil cursorXSmoother;
    @Shadow
    private final SmoothUtil cursorYSmoother;
    @Shadow
    private double x;
    @Shadow
    private double y;
    @Shadow
    private double eventDeltaWheel;

    public MouseMixin() {
        lastMouseUpdateTime = Double.MIN_VALUE;
        cursorXSmoother = new SmoothUtil();
        cursorYSmoother = new SmoothUtil();
    }

    @Inject(method = { "updateMouse" }, at = { @At("HEAD") }, cancellable = true)
    private void noPlayerRotation(final CallbackInfo info) {
        final double time = GlfwUtil.getTime();
        final double min = time - lastMouseUpdateTime;
        lastMouseUpdateTime = time;
        if (isCursorLocked() && client.isWindowFocused()) {
            final double sens = client.options.mouseSensitivity * 0.6000000238418579 + 0.20000000298023224;
            final double mult = sens * sens * sens * 8.0;
            double deltaX;
            double deltaY;
            if (client.options.smoothCameraEnabled) {
                final double smoothX = cursorXSmoother.smooth(cursorDeltaX * mult, min * mult);
                final double smoothY = cursorYSmoother.smooth(cursorDeltaY * mult, min * mult);
                deltaX = smoothX;
                deltaY = smoothY;
            }
            else {
                cursorXSmoother.clear();
                cursorYSmoother.clear();
                deltaX = cursorDeltaX * mult;
                deltaY = cursorDeltaY * mult;
            }
            if (Mods.PERSPECTIVE_MOD.enabled) {
                final PerspectiveMod instance = Mods.PERSPECTIVE_MOD;
                instance.cameraYaw += (float)(deltaX / 8.0);
                final PerspectiveMod instance2 = Mods.PERSPECTIVE_MOD;
                instance2.cameraPitch += (float)(deltaY / 8.0);
                if (Math.abs(Mods.PERSPECTIVE_MOD.cameraPitch) > 90.0f) {
                    Mods.PERSPECTIVE_MOD.cameraPitch = ((Mods.PERSPECTIVE_MOD.cameraPitch > 0.0f) ? 90.0f : -90.0f);
                }
            }
            cursorDeltaX = 0.0;
            cursorDeltaY = 0.0;
            int yDir = 1;
            if (client.options.invertYMouse) {
                yDir = -1;
            }
            client.getTutorialManager().onUpdateMouse(deltaX, deltaY);
            if (client.player != null && !Mods.PERSPECTIVE_MOD.enabled) {
                client.player.changeLookDirection(deltaX, deltaY * yDir);
            }
        }
        else {
            cursorDeltaX = 0.0;
            cursorDeltaY = 0.0;
        }
        info.cancel();
    }

    @Inject(method = { "onMouseScroll" }, at = { @At("HEAD") }, cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, final CallbackInfo info) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
            double d = (this.client.options.discreteMouseScroll ? Math.signum(vertical) : vertical) * this.client.options.mouseWheelSensitivity;
            if (this.client.getOverlay() == null) {
                if (this.client.currentScreen != null) {
                    double e = this.x * (double)this.client.getWindow().getScaledWidth() / (double)this.client.getWindow().getWidth();
                    double f = this.y * (double)this.client.getWindow().getScaledHeight() / (double)this.client.getWindow().getHeight();
                    this.client.currentScreen.mouseScrolled(e, f, d);
                } else if (this.client.player != null) {
                    if (this.eventDeltaWheel != 0.0D && Math.signum(d) != Math.signum(this.eventDeltaWheel)) {
                        this.eventDeltaWheel = 0.0D;
                    }

                    this.eventDeltaWheel += d;
                    float g = (float)((int)this.eventDeltaWheel);
                    if (g == 0.0F) {
                        info.cancel();
                        return;
                    }

                    if (Mods.ZOOM_MOD.enabled){
                        System.out.println(g);
                        Mods.ZOOM_MOD.addFOV(-g);
                        info.cancel();
                        return;
                    }

                    this.eventDeltaWheel -= (double)g;
                    if (this.client.player.isSpectator()) {
                        if (this.client.inGameHud.getSpectatorHud().isOpen()) {
                            this.client.inGameHud.getSpectatorHud().cycleSlot((double)(-g));
                        } else {
                            float h = MathHelper.clamp(this.client.player.getAbilities().getFlySpeed() + g * 0.005F, 0.0F, 0.2F);
                            this.client.player.getAbilities().setFlySpeed(h);
                        }
                    } else {
                        this.client.player.getInventory().scrollInHotbar((double)g);
                    }
                }
            }
        }
        info.cancel();
    }

    @Shadow
    public boolean isCursorLocked() {
        return cursorLocked;
    }
}
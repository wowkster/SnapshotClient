package com.adrianwowk.snapshotclient.mixin;

import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow
    private boolean ready;
    @Shadow
    private BlockView area;
    @Shadow
    private Entity focusedEntity;
    @Shadow
    private float pitch;
    @Shadow
    private float yaw;
    @Shadow
    private boolean thirdPerson;
    @Shadow
    private float cameraY;
    @Shadow
    private float lastCameraY;
    @Shadow
    private Vec3d pos;
    @Shadow
    private Vec3f horizontalPlane;

    @Inject(method = {"update"}, at = {@At("HEAD")}, cancellable = true)
    private void updateCamera(final BlockView area, final Entity entity, final boolean thirdPerson, final boolean inverseView, final float tickDelta, final CallbackInfo info) {
        if (Mods.PERSPECTIVE_MOD.enabled) {
            this.ready = true;
            this.area = area;
            this.focusedEntity = entity;
            this.thirdPerson = thirdPerson;
            MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_BACK);
            this.setRotation(Mods.PERSPECTIVE_MOD.cameraYaw, Mods.PERSPECTIVE_MOD.cameraPitch);
            this.setPos(MathHelper.lerp((double) tickDelta, this.focusedEntity.prevX, this.focusedEntity.getX()), MathHelper.lerp((double) tickDelta, focusedEntity.prevY, focusedEntity.getY()) + MathHelper.lerp(tickDelta, this.lastCameraY, this.cameraY), MathHelper.lerp((double) tickDelta, this.focusedEntity.prevZ, this.focusedEntity.getZ()));
            this.pitch = Mods.PERSPECTIVE_MOD.cameraPitch;
            this.yaw = Mods.PERSPECTIVE_MOD.cameraYaw;
            this.moveBy(-this.clipToSpace(4.0), 0.0, 0.0);
            info.cancel();
        }
    }

    @Inject(method = {"clipToSpace"}, at = {@At("HEAD")}, cancellable = true)
    private void clipSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> cir) {
        for(int i = 0; i < 8; ++i) {
            float f = (float)((i & 1) * 2 - 1);
            float g = (float)((i >> 1 & 1) * 2 - 1);
            float h = (float)((i >> 2 & 1) * 2 - 1);
            f *= 0.1F;
            g *= 0.1F;
            h *= 0.1F;
            Vec3d vec3d = this.pos.add((double)f, (double)g, (double)h);
            Vec3d vec3d2 = new Vec3d(this.pos.x - (double)this.horizontalPlane.getX() * desiredCameraDistance + (double)f + (double)h, this.pos.y - (double)this.horizontalPlane.getY() * desiredCameraDistance + (double)g, this.pos.z - (double)this.horizontalPlane.getZ() * desiredCameraDistance + (double)h);
            HitResult hitResult = this.area.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, this.focusedEntity));
            if (!Mods.PERSPECTIVE_MOD.enabled || !Mods.PERSPECTIVE_MOD.cameraNoClip) {
                if (hitResult.getType() != HitResult.Type.MISS) {
                    double d = hitResult.getPos().distanceTo(this.pos);
                    if (d < desiredCameraDistance) {
                        desiredCameraDistance = d;
                    }
                }
            }
        }

        cir.setReturnValue(desiredCameraDistance);
    }

    @Shadow
    protected void setRotation(final float yaw, final float pitch) {
    }

    @Shadow
    protected void setPos(final double x, final double y, final double z) {
    }

    @Shadow
    protected void moveBy(final double x, final double y, final double z) {
    }

    @Shadow
    private double clipToSpace(final double desiredCameraDistance) {
        return desiredCameraDistance;
    }
}
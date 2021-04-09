package com.adrianwowk.snapshotclient.mixin;

import com.adrianwowk.snapshotclient.access.PlayerEntityModelAccess;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> implements PlayerEntityModelAccess {

    public ModelPart customCloak;

    private PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(ModelPart root, boolean thinArms, CallbackInfo ci){
        customCloak = root.getChild("custom_cloak");
    }

    // Overwrites the getTexturedModelData() method
    @Inject(method = "getTexturedModelData", at = @At("HEAD"), cancellable = true)
    private static void inject(Dilation dilation, boolean slim, CallbackInfoReturnable<ModelData> cir) {
        ModelData modelData = BipedEntityModel.getModelData(dilation, 0.0F);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("ear", ModelPartBuilder.create().uv(24, 0).cuboid(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, dilation), ModelTransform.NONE);
        modelPartData.addChild("cloak", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, dilation, 1.0F, 0.5F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild("custom_cloak", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, dilation, 0.695652174F, 0.727272727F * 0.5F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        float f = 0.25F;
        if (slim) {
            modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
            modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));
            modelPartData.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(5.0F, 2.5F, 0.0F));
            modelPartData.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-5.0F, 2.5F, 0.0F));
        } else {
            modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
            modelPartData.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
            modelPartData.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        }

        modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        modelPartData.addChild("left_pants", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        modelPartData.addChild("right_pants", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
        modelPartData.addChild("jacket", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);
        cir.setReturnValue(modelData);
    }

    @Override
    public ModelPart getCustomCloak() {
        return this.customCloak;
    }

    @Override
    public void setCustomCloak(ModelPart mp) {
        this.customCloak = mp;
    }
}

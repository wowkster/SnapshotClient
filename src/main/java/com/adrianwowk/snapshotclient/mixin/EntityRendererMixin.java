package com.adrianwowk.snapshotclient.mixin;

import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @Final
    @Shadow
    protected EntityRenderDispatcher dispatcher;

    @Inject(method = "renderLabelIfPresent", at = {@At("HEAD")}, cancellable = true)
    public void inject(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (!(d > 4096.0D)) {
            boolean bl = !entity.isSneaky();
            float f = entity.getHeight() + 0.5F;
            int i = "deadmau5".equals(text.getString()) ? -10 : 0;
            matrices.push();
            matrices.translate(0.0D, (double)f, 0.0D);
            matrices.multiply(this.dispatcher.getRotation());
            double scale = Mods.NAME_TAG_MOD.nameTagScale;
            matrices.scale(-0.025F * (float)scale, -0.025F * (float)scale, 0.025F * (float)scale);
            Matrix4f matrix4f = matrices.peek().getModel();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(g * 255.0F) << 24;
            TextRenderer textRenderer = this.getFontRenderer();
            float h = (float)(-textRenderer.getWidth((StringVisitable)text) / 2);
            textRenderer.draw(text, h, (float)i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
            if (bl) {
                textRenderer.draw((Text)text, h, (float)i, -1, false, matrix4f, vertexConsumers, false, 0, light);
            }

            matrices.pop();
        }
        ci.cancel();
    }

    @Shadow
    public TextRenderer getFontRenderer() {
        return null;
    }

}

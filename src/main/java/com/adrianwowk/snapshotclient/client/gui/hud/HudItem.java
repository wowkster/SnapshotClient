package com.adrianwowk.snapshotclient.client.gui.hud;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public abstract class HudItem {
    public double x;
    public double xPer;
    public double y;
    public double yPer;
    public double width;
    public double height;
    public boolean beingDragged = false;
    protected TextRenderer textRenderer;

    protected HudItem(){
        x = (int)(Math.random() * 100);
        y = 10;
        width = 0;
        height = 0;
    }

    public void render(MatrixStack matrixStack, float tickDelta){
        if (textRenderer == null)
            textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    public abstract boolean shouldRender();

    public void setPer(double xPer_, double yPer_){
    }

    public void computeDimensions(){
        int scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

        if (scaledWidth < width || scaledHeight < height)
            return;

        x = xPer * scaledWidth;
        y = yPer * scaledHeight;

        if (xPer + xToPer(width) > 1)
            setPer(xToPer(scaledWidth - width), yPer);
        if (yPer + yToPer(height) > 1)
            setPer(xPer, yToPer(scaledHeight - height));
    }

    public boolean mouseOver(double mouseX, double mouseY){
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean deltaOutOfBounds(double deltaX, double deltaY){
        int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

        return x + width + deltaX > windowWidth || x + deltaX < 0 || y + height + deltaY > windowHeight || y + deltaY < 0 ;
    }

    public double xToPer(double x){
        return x / MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public double yToPer(double y){
        return y / MinecraftClient.getInstance().getWindow().getScaledHeight();
    }
}

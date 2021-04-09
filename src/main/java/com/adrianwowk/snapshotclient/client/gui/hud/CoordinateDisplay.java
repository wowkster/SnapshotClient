package com.adrianwowk.snapshotclient.client.gui.hud;

import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.Mods;
import com.oroarmor.config.ConfigItem;
import net.minecraft.client.util.math.MatrixStack;

public class CoordinateDisplay extends HudItem {
    public CoordinateDisplay() {
        xPer = ((ConfigItem<Double>) ClientConfig.GuiConfig.coordinateDisplay.getConfigs().get(0)).getValue();
        yPer = ((ConfigItem<Double>)ClientConfig.GuiConfig.coordinateDisplay.getConfigs().get(1)).getValue();
    }

    @Override
    public void render(MatrixStack matrixStack, float tickDelta) {
        super.render(matrixStack, tickDelta);

        textRenderer.drawWithShadow(matrixStack, Mods.COORDINATE_MOD.getHudDisplay(), (int) x,(int) y, 0xffffff);

        computeDimensions();
    }

    @Override
    public boolean shouldRender() {
        return Mods.COORDINATE_MOD.enabled;
    }

    @Override
    public void setPer(double xPer_, double yPer_) {
        ((ConfigItem<Double>)ClientConfig.GuiConfig.coordinateDisplay.getConfigs().get(0)).setValue(xPer);
        ((ConfigItem<Double>)ClientConfig.GuiConfig.coordinateDisplay.getConfigs().get(1)).setValue(yPer);
        super.setPer(xPer_,yPer_);
    }

    @Override
    public void computeDimensions() {
        width = textRenderer.getWidth(Mods.COORDINATE_MOD.getHudDisplay().asString());
        height = textRenderer.fontHeight;

        super.computeDimensions();
    }
}

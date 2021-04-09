package com.adrianwowk.snapshotclient.client.gui.hud;

import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.Mod;
import com.adrianwowk.snapshotclient.client.mods.Mods;
import com.oroarmor.config.ConfigItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class DebugText extends HudItem {
    private ArrayList<Text> renderList;

    public DebugText() {
        super();
        renderList = new ArrayList<>();

        xPer = ((ConfigItem<Double>)ClientConfig.GuiConfig.hudDisplay.getConfigs().get(0)).getValue();
        yPer = ((ConfigItem<Double>)ClientConfig.GuiConfig.hudDisplay.getConfigs().get(1)).getValue();
        width = 0;
        height = 0;
    }

    @Override
    public void render(MatrixStack matrixStack, float tickDelta) {
        super.render(matrixStack, tickDelta);

        renderList.clear();
        for (Mod mod : Mods.MODS)
            renderList.add(mod.getDesc());

        int yDelta = 0;
        for (Text text : renderList){
            if (text.asString().equals(""))
                continue;
            textRenderer.drawWithShadow(matrixStack, text, (int) x,(int) y + yDelta * (textRenderer.fontHeight), 0xffffff);
            yDelta++;
        }

        computeDimensions();
    }

    @Override
    public boolean shouldRender() {
        return ClientConfig.ConfigGroup.renderHud.getValue();
    }

    @Override
    public void setPer(double xPer_, double yPer_) {
        ((ConfigItem<Double>)ClientConfig.GuiConfig.hudDisplay.getConfigs().get(0)).setValue(xPer);
        ((ConfigItem<Double>)ClientConfig.GuiConfig.hudDisplay.getConfigs().get(1)).setValue(yPer);
        super.setPer(xPer_,yPer_);
    }

    @Override
    public void computeDimensions() {
        int maxWidth = 0;
        int yDelta = 0;
        for (Text text : renderList){
            if (text.asString().equals(""))
                continue;
            yDelta++;
            int textWidth = textRenderer.getWidth(text.asString());
            if (textWidth > maxWidth)
                maxWidth = textWidth;
        }

        width = maxWidth;
        height = yDelta * (textRenderer.fontHeight);

        super.computeDimensions();
    }
}

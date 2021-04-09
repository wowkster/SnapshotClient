package com.adrianwowk.snapshotclient.client.gui.hud;

import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.gui.screen.GuiEditorScreen;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class ModHud {
    public static boolean renderHud;
    public List<HudItem> hudItems;

    public ModHud(){
        hudItems = new ArrayList<>();
        hudItems.add(new DebugText());
        hudItems.add(new FpsDisplay());
        hudItems.add(new CoordinateDisplay());

        renderHud = ClientConfig.ConfigGroup.renderHud.getValue();

        initRenderEvent();
    }

    private void initRenderEvent() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (MinecraftClient.getInstance().options.debugEnabled)
                return;
            if (MinecraftClient.getInstance().currentScreen instanceof GuiEditorScreen)
                return;

                for (int i = hudItems.size() - 1; i >= 0; i--) {
                    if (hudItems.get(i).shouldRender())
                        hudItems.get(i).render(matrixStack, tickDelta);
                }
        });
    }
}

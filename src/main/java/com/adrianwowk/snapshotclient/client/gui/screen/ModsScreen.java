package com.adrianwowk.snapshotclient.client.gui.screen;

import com.adrianwowk.snapshotclient.client.gui.widgets.ModListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class ModsScreen extends GameOptionsScreen {

    private ModListWidget modList;

    public ModsScreen() {
        super(null, MinecraftClient.getInstance().options, new TranslatableText("options.mods_title"));
    }

    protected void init() {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27 - 60, 200, 20, ScreenTexts.DONE, (button) -> {
            this.client.openScreen(null);
        }));

        this.modList = new ModListWidget(this.client, this.width, this.height, 30, this.height - 30, 45, this);
        this.children.add(this.modList);
        for (ModListWidget.Entry entry : modList.children()){
            this.addChild2(((ModListWidget.ModEntry)entry).buttonList);
        }
    }

    public <T extends AbstractButtonWidget> T addButton2(T button) {
        this.buttons.add(button);
        return this.addChild(button);
    }

    public void addChild2(Element e){
        this.children.add(e);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        this.modList.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
//        buttons.get(0).render(matrices, mouseX, mouseY, delta);
    }
}

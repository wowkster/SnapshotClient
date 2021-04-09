package com.adrianwowk.snapshotclient.client.gui.widgets;

import com.adrianwowk.snapshotclient.client.gui.screen.ModsScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ModWidget extends AbstractButtonWidget implements Drawable {

    public ButtonListWidget list;
    public ModsScreen parent;
    public String title;
    private TextRenderer renderer;

    public ModWidget(ModsScreen parent, String title, Option[] options, TextRenderer renderer, int x, int y, int width, int height) {
        super(x, y, width, height, Text.of(""));
        this.parent = parent;
        this.title = title;
        this.renderer = renderer;
        init(options);
    }

    public void init(Option[] options){
//        this.list = new ModListWidget(MinecraftClient.getInstance(), this.width, this.height, y, y + 30, 25);
        list.addAll(options);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta){
        list.render(matrices, mouseX, mouseY, delta);
        drawCenteredString(matrices,  this.renderer, this.title, this.parent.width/2, this.y - 20, 0xffffff);
    }

}

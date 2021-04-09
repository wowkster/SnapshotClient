package com.adrianwowk.snapshotclient.client.gui.widgets;

import com.adrianwowk.snapshotclient.client.gui.screen.ModsScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.adrianwowk.snapshotclient.client.ClientOptions.*;

public class ModListWidget extends ElementListWidget<ModListWidget.Entry> {

    ModsScreen screen;
    public List<AbstractButtonWidget> configButtons = new ArrayList<>();
    public List<AbstractButtonWidget> buttons = new ArrayList<>();

    public ModListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, ModsScreen screen) {
        super(client, width, height, top, bottom, itemHeight);
        this.screen = screen;
        boolean[] persModsShrts = new boolean[] {true, true, true, true, false};
        this.addEntry(new ModListWidget.ModEntry(screen, new Option[]{FPS_ON,FULL_BRIGHT,CLEAR_WATER,COORDINATES, RENDER_HUD}, ""/*"Persistent Mods"*/, this.width, 1000, 0, persModsShrts));
        this.addEntry(new ModListWidget.ModEntry(screen, new Option[]{ZOOM_FOV, SMOOTH_CAMERA}, "Zoom", this.width, 1000, 25 * 2,  new boolean[] {false, false}));
        this.addEntry(new ModListWidget.ModEntry(screen, new Option[]{NAME_TAG_SCALE, NAME_TAG_F5}, "Nametag Changer", this.width, 1000,  25 * 2,  new boolean[] {false, false}));

        ModEntry firstEntry = (ModEntry)children().get(0);

        int count = -1;
        for (ClearButtonListWidget.ButtonEntry entry : firstEntry.buttonList.children()) {
            for (AbstractButtonWidget button : entry.buttons){
                count++;
                if (!persModsShrts[count])
                    continue;
                buttons.add(button);

                AbstractButtonWidget config = new ButtonWidget(button.x + button.getWidth() + 2, button.y, 20, 20, Text.of("..."), (btn) -> {
                    System.out.println("Clicked Config");
                });

                configButtons.add(config);
                screen.addButton2(config);
            }
        }

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        super.render(matrices, mouseX, mouseY, delta);
//
//        if (true)
//            return;

        int i = this.getScrollbarPositionX();
        int j = i + 6;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        int k = this.getRowLeft();
        int l = this.top + 4 - (int)this.getScrollAmount();

        this.renderList(matrices, k, l, mouseX, mouseY, delta);

        int o = this.getMaxScroll();
        if (o > 0) {
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            int p = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getMaxPosition());
            p = MathHelper.clamp(p, 32, this.bottom - this.top - 8);
            int q = (int)this.getScrollAmount() * (this.bottom - this.top - p) / o + this.top;
            if (q < this.top) {
                q = this.top;
            }

            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex((double)i, (double)this.bottom, 0.0D).color(0, 0, 0, 255).next();
            bufferBuilder.vertex((double)j, (double)this.bottom, 0.0D).color(0, 0, 0, 255).next();
            bufferBuilder.vertex((double)j, (double)this.top, 0.0D).color(0, 0, 0, 255).next();
            bufferBuilder.vertex((double)i, (double)this.top, 0.0D).color(0, 0, 0, 255).next();
            bufferBuilder.vertex((double)i, (double)(q + p), 0.0D).color(128, 128, 128, 255).next();
            bufferBuilder.vertex((double)j, (double)(q + p), 0.0D).color(128, 128, 128, 255).next();
            bufferBuilder.vertex((double)j, (double)q, 0.0D).color(128, 128, 128, 255).next();
            bufferBuilder.vertex((double)i, (double)q, 0.0D).color(128, 128, 128, 255).next();
            bufferBuilder.vertex((double)i, (double)(q + p - 1), 0.0D).color(192, 192, 192, 255).next();
            bufferBuilder.vertex((double)(j - 1), (double)(q + p - 1), 0.0D).color(192, 192, 192, 255).next();
            bufferBuilder.vertex((double)(j - 1), (double)q, 0.0D).color(192, 192, 192, 255).next();
            bufferBuilder.vertex((double)i, (double)q, 0.0D).color(192, 192, 192, 255).next();
            tessellator.draw();
        }

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 50;
    }

    @Environment(EnvType.CLIENT)
    public class ModEntry extends ModListWidget.Entry {
        public String title;
        public ClearButtonListWidget buttonList;
        ModsScreen screen;
        private int width;
        private int height;
        private int y;

        public ModEntry(ModsScreen screen, Option[] options, String text, int width, int height, int yOffest, boolean[] shrts) {
            title = text;
            this.width = width;
            this.height = height;
            this.y = yOffest;
            this.screen = screen;

            buttonList = new ClearButtonListWidget(MinecraftClient.getInstance(), this.width, this.height, y, y + this.height + 1, 25);
            if (options.length % 2 == 0) {
                buttonList.addAll(options, shrts);
            } else {
                Option[] options_ = new Option[options.length - 1];
                for (int i = 0; i < options_.length; i++)
                    options_[i] = options[i];
                buttonList.addAll(options_, shrts);
                buttonList.addSingleOptionEntry(options[options.length-1], shrts[shrts.length-1]);
            }

            for (ClearButtonListWidget.ButtonEntry entry : buttonList.children()) {
                for (AbstractButtonWidget button : entry.buttons){
                    screen.addButton2(button);
                }
            }
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            buttonList.top = y + this.y;
            buttonList.render(matrices, mouseX, mouseY, tickDelta);

            for (int i = 0; i < buttons.size(); i++){
                configButtons.get(i).y = buttons.get(i).y;
            }

            drawCenteredString(matrices,  ModListWidget.this.client.textRenderer, this.title, this.width/2, y - 10 + this.y, 0xffffff);
        }

        public boolean changeFocus(boolean lookForwards) {
            return false;
        }

        public List<? extends Element> children() {
            return Collections.emptyList();
        }
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<ModListWidget.Entry> {
    }
}

package com.adrianwowk.snapshotclient.client.gui.widgets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ClearButtonListWidget extends ElementListWidget<ClearButtonListWidget.ButtonEntry> {
    public ClearButtonListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
    }

    public int addSingleOptionEntry(Option option, boolean shrt) {
        return this.addEntry(ClearButtonListWidget.ButtonEntry.create(this.client.options, this.width, option, shrt));
    }

    public void addOptionEntry(Option firstOption, @Nullable Option secondOption, boolean shrt1, boolean shrt2) {
        this.addEntry(ClearButtonListWidget.ButtonEntry.create(this.client.options, this.width, firstOption, secondOption, shrt1, shrt2));
    }

    public void addAll(Option[] options, boolean[] shrts) {
        for(int i = 0; i < options.length; i += 2) {
            this.addOptionEntry(options[i], i < options.length - 1 ? options[i + 1] : null, shrts[i], shrts[i + 1]);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
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

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 32;
    }

    @Nullable
    public AbstractButtonWidget getButtonFor(Option option) {
        Iterator var2 = this.children().iterator();

        AbstractButtonWidget abstractButtonWidget;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            ClearButtonListWidget.ButtonEntry buttonEntry = (ClearButtonListWidget.ButtonEntry)var2.next();
            abstractButtonWidget = (AbstractButtonWidget)buttonEntry.field_27983.get(option);
        } while(abstractButtonWidget == null);

        return abstractButtonWidget;
    }

    public Optional<AbstractButtonWidget> getHoveredButton(double mouseX, double mouseY) {
        Iterator var5 = this.children().iterator();

        while(var5.hasNext()) {
            net.minecraft.client.gui.widget.ButtonListWidget.ButtonEntry buttonEntry = (net.minecraft.client.gui.widget.ButtonListWidget.ButtonEntry)var5.next();
            Iterator var7 = buttonEntry.buttons.iterator();

            while(var7.hasNext()) {
                AbstractButtonWidget abstractButtonWidget = (AbstractButtonWidget)var7.next();
                if (abstractButtonWidget.isMouseOver(mouseX, mouseY)) {
                    return Optional.of(abstractButtonWidget);
                }
            }
        }

        return Optional.empty();
    }

    @Environment(EnvType.CLIENT)
    public static class ButtonEntry extends ElementListWidget.Entry<ClearButtonListWidget.ButtonEntry> {
        private final Map<Option, AbstractButtonWidget> field_27983;
        public final List<AbstractButtonWidget> buttons;

        private ButtonEntry(Map<Option, AbstractButtonWidget> map) {
            this.field_27983 = map;
            this.buttons = ImmutableList.copyOf(map.values());
        }

        public static ClearButtonListWidget.ButtonEntry create(GameOptions options, int width, Option option,boolean shrt) {
            return new ClearButtonListWidget.ButtonEntry(ImmutableMap.of(option, option.createButton(options, width / 2 - 155, 0, (shrt ? 288 : 310))));
        }

        public static ClearButtonListWidget.ButtonEntry create(GameOptions options, int width, Option firstOption, @Nullable Option secondOption, boolean shrt1, boolean shrt2) {
            AbstractButtonWidget abstractButtonWidget = firstOption.createButton(options, width / 2 - 155, 0, (shrt1 ? 128 : 150));
            return secondOption == null ? new ClearButtonListWidget.ButtonEntry(ImmutableMap.of(firstOption, abstractButtonWidget)) : new ClearButtonListWidget.ButtonEntry(ImmutableMap.of(firstOption, abstractButtonWidget, secondOption, secondOption.createButton(options, width / 2 - 155 + 160, 0, (shrt2 ? 128 : 150))));
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.buttons.forEach((button) -> {
                button.y = y;
                button.render(matrices, mouseX, mouseY, tickDelta);
            });
        }

        public List<? extends Element> children() {
            return this.buttons;
        }
    }
}
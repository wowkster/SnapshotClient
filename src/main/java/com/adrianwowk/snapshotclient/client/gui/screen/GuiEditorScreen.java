package com.adrianwowk.snapshotclient.client.gui.screen;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.gui.hud.HudItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GuiEditorScreen extends Screen {
    Screen parent;
    boolean mouseDown = false;

    public GuiEditorScreen(Screen parent) {
        super(Text.of("GUI Editor"));

        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27 - 60, 200, 20, ScreenTexts.DONE, (button) -> {
            this.client.openScreen(parent);
            for (HudItem item : SnapshotclientClient.HUD.hudItems){
                item.xPer = 0;
                item.yPer = 0;
            }
        }));
    }

    @Override
    public void onClose() {
        SnapshotclientClient.CONFIG.saveConfigToFile();
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);

        for (int i = SnapshotclientClient.HUD.hudItems.size() - 1; i >= 0; i--) {
            HudItem item = SnapshotclientClient.HUD.hudItems.get(i);
            if (item.shouldRender()) {
                renderBoxAroundItem(matrices, item);
                item.render(matrices, 0);
            }
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, -0x8FEFEFF0, -0x7FEFEFF0);
    }

    public void renderBoxAroundItem(MatrixStack matrices, HudItem item){
        int topY = (int)item.y - 3;
        int leftX = (int)item.x - 3;
        int bottomY = (int)item.y + 1 + (int)item.height;
        int rightX = (int)item.x + (int)item.width + 2;

        fillGradient(matrices, leftX, topY, rightX, bottomY, -0x6FEFEFF0, -0x5FEFEFF0);

        drawVerticalLine(matrices, leftX, topY, bottomY, -0x111111); // Left
        drawHorizontalLine(matrices, leftX, rightX, topY, -0x111111); // Top
        drawVerticalLine(matrices,  rightX, topY, bottomY, -0x111111); // Right
        drawHorizontalLine(matrices, leftX, rightX, bottomY, -0x111111); // Bottom
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (HudItem item : SnapshotclientClient.HUD.hudItems){
            if (item.shouldRender()) {
                if (mouseDown && mouseInWindow(mouseX, mouseY) && item.beingDragged && !item.deltaOutOfBounds(deltaX, deltaY)) {
                    item.xPer += item.xToPer(deltaX);
                    item.yPer += item.yToPer(deltaY);
                    item.setPer(item.xToPer(deltaX), item.yToPer(deltaY));
                }

            }
        }

        return true;
    }

    private boolean mouseInWindow(double mouseX, double mouseY) {
        int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

        return mouseX > 0 && mouseX < windowWidth && mouseY > 0 && mouseY < windowHeight;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (HudItem item : SnapshotclientClient.HUD.hudItems){
            if (item.shouldRender()) {
                if (item.mouseOver(mouseX, mouseY)) {
                    mouseDown = true;
                    item.beingDragged = true;
                    break;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mouseDown = false;
        for (HudItem item : SnapshotclientClient.HUD.hudItems){
            item.beingDragged = false;
        }
        return true;
    }
}

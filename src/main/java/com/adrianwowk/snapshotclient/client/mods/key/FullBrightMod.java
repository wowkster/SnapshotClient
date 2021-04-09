package com.adrianwowk.snapshotclient.client.mods.key;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.KeyMod;
import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class FullBrightMod extends KeyMod {

    private double startBrightness = 1.0;

    public FullBrightMod() {
        super(GLFW.GLFW_KEY_M, "full_bright");
        enabled = ClientConfig.ConfigGroup.fullBright.getValue();

        options = new Option[] {CyclingOption.create("options.full_bright",
                (gameOptions) -> Mods.FULL_BRIGHT_MOD.enabled, (gameOptions, option, fullbright) -> {
                    Mods.FULL_BRIGHT_MOD.setEnabled(fullbright);
                    ClientConfig.ConfigGroup.fullBright.setValue(fullbright);
                    SnapshotclientClient.CONFIG.saveConfigToFile();
                })};

    }

    public void keyUp() {

    }

    public void keyDown() {
        if (!enabled){
            enabled = true;
            startBrightness = client.options.gamma;
            client.options.gamma = 100.0;
        } else {
            enabled = false;
            if (startBrightness <= 1)
                client.options.gamma = startBrightness;
            else
                client.options.gamma = 1.0;
        }
    }

    public Text getDesc() {
        return Text.of("§eFullbright:§f " + (enabled ? "On" : "Off"));
    }

    @Override
    public void setEnabled(boolean bright){
        enabled = !bright;
        keyDown();
        ClientConfig.ConfigGroup.fullBright.setValue(bright);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }

}

package com.adrianwowk.snapshotclient.client.mods.persistent;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class FPSMod extends Mod {

    public FPSMod(){
        enabled = ClientConfig.ConfigGroup.fpsDisplay.getValue();
    }

    @Override
    public Text getDesc() {
        return Text.of("§eFPS Display:§f " + (enabled ? "On" : "Off"));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        ClientConfig.ConfigGroup.fpsDisplay.setValue(enabled);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }

    public Text getHudDisplay(){
        return Text.of((enabled) ? "§eFPS:§f " + MinecraftClient.getInstance().fpsDebugString.split(" ")[0] : "");
    }
}

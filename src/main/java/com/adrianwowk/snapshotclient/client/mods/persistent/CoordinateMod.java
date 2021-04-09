package com.adrianwowk.snapshotclient.client.mods.persistent;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CoordinateMod extends Mod {
    public CoordinateMod(){
        enabled = ClientConfig.ConfigGroup.coordinateDisplay.getValue();
    }

    @Override
    public Text getDesc() {
        return Text.of("§eFPS Display:§f " + (enabled ? "On" : "Off"));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        ClientConfig.ConfigGroup.coordinateDisplay.setValue(enabled);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }

    public Text getHudDisplay(){
        return Text.of((enabled) ? String.format("§eCoordinates:§f §7X:§f %d §7Y:§f %d §7Z:§f %d", client.player.getBlockX(), client.player.getBlockY(), client.player.getBlockZ()) : "");
    }
}

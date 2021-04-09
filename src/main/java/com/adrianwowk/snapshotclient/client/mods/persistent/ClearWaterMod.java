package com.adrianwowk.snapshotclient.client.mods.persistent;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.Mod;
import net.minecraft.text.Text;

public class ClearWaterMod extends Mod {
    public ClearWaterMod(){
        enabled = ClientConfig.ConfigGroup.clearWater.getValue();
    }

    @Override
    public Text getDesc() {
        return Text.of("§eClear Water:§f " + (enabled ? "On" : "Off"));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        ClientConfig.ConfigGroup.clearWater.setValue(enabled);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }
}

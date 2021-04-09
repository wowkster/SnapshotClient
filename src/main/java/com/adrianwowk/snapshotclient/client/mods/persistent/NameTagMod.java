package com.adrianwowk.snapshotclient.client.mods.persistent;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.Mod;
import net.minecraft.text.Text;

public class NameTagMod extends Mod {

    public double nameTagScale;

    public NameTagMod(){
        enabled = ClientConfig.ConfigGroup.nameTagF5.getValue();
        nameTagScale = ClientConfig.ConfigGroup.nameTagScale.getValue();
    }

    @Override
    public Text getDesc() {
        return Text.of("§eNameTag in F5:§f " + (enabled ? "On" : "Off") + " §7| §eScale:§f " + String.format("%.01fx", nameTagScale));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        ClientConfig.ConfigGroup.nameTagF5.setValue(enabled);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }

    public void setNameTagScale(double scale){
        this.nameTagScale = scale;
        ClientConfig.ConfigGroup.nameTagScale.setValue(scale);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }
}

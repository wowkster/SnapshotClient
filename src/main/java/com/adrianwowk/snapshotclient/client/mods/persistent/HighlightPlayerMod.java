package com.adrianwowk.snapshotclient.client.mods.persistent;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.Mod;
import net.minecraft.text.Text;

public class HighlightPlayerMod extends Mod {

    public HighlightPlayerMod(){
        enabled = ClientConfig.ConfigGroup.highlightPlayers.getValue();
    }

    @Override
    public Text getDesc() {
        return Text.of("§eHighlight Players:§f " + (enabled ? "On" : "Off"));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        ClientConfig.ConfigGroup.highlightPlayers.setValue(enabled);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }
}

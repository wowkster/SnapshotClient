package com.adrianwowk.snapshotclient.client.mods.persistent;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CapeMod extends Mod {
    public Cape current = null;

    public CapeMod(){
        enabled = ClientConfig.ConfigGroup.capeMod.getValue();
    }

    @Override
    public Text getDesc() {
        return Text.of("§eCape Mod:§f " + (enabled ? "On" : "Off") + " §7| §eCape: " + (current == null ? "None" : current));
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        ClientConfig.ConfigGroup.capeMod.setValue(enabled);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }

    public enum Cape {
        MINECON("Minecon 2013", new Identifier("sclient:capes/minecon_2013.png")), WOWKSTER("Wowkster", new Identifier("sclient:capes/wowkster1.png"));

        private String name;
        private Identifier id;

        Cape(String name, Identifier id){
            this.name = name;
            this.id = id;
        }

        public String getName(){
            return this.name;
        }

        public Identifier getId(){
            return this.id;
        }
    }
}

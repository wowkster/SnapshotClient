package com.adrianwowk.snapshotclient.client.mods;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;

public abstract class Mod {
    public MinecraftClient client;
    public boolean enabled;
    public Option[] options;

    protected Mod(){
        this.client = MinecraftClient.getInstance();
    }

    public abstract Text getDesc();

    public abstract void setEnabled(boolean enabled);
}

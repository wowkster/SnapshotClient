package com.adrianwowk.snapshotclient.client.mods;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public abstract class KeyMod extends Mod {

    public boolean lastPressed;
    public KeyBinding key;

    protected KeyMod(int keyCode, String desc) {
        super();
        enabled = false;

        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sclient." + desc, // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                keyCode, // The keycode of the key
                "category.sclient.keys" // The translation key of the keybinding's category.
        ));

        init();
    }
    public abstract void keyUp();

    public abstract void keyDown();

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void init(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!key.isPressed() && lastPressed){
                lastPressed = false;
                keyUp();
            }
            if(key.isPressed()) {
                if (!lastPressed){
                    lastPressed = true;
                    keyDown();
                }
            }
        });
    }
}

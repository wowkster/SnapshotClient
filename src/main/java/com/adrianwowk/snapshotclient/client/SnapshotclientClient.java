package com.adrianwowk.snapshotclient.client;

import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.gui.hud.ModHud;
import com.adrianwowk.snapshotclient.client.gui.screen.GuiEditorScreen;
import com.adrianwowk.snapshotclient.client.gui.screen.ModsScreen;
import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class SnapshotclientClient implements ClientModInitializer {

    private static KeyBinding modsMenu;
    private static KeyBinding guiEditor;
    public static ClientConfig CONFIG;
    public static ModHud HUD;

    @Override
    public void onInitializeClient() {
        CONFIG = new ClientConfig();

        CONFIG.readConfigFromFile();
        CONFIG.saveConfigToFile();

        HUD = new ModHud();

        Mods.init();

        registerKeyBindings();
    }

    private void registerKeyBindings(){
        modsMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sclient.mods", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_RIGHT_SHIFT, // The keycode of the key
                "category.sclient.keys" // The translation key of the keybinding's category.
        ));

        guiEditor = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sclient.gui_editor", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_BACKSLASH, // The keycode of the key
                "category.sclient.keys" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (modsMenu.isPressed()){
                client.openScreen(new ModsScreen());
            }

            while (guiEditor.isPressed()){
                client.openScreen(new GuiEditorScreen(null));
            }
        });
    }

}

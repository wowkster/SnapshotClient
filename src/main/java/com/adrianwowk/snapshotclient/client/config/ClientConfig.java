package com.adrianwowk.snapshotclient.client.config;

import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItem;
import com.oroarmor.config.ConfigItemGroup;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClientConfig extends Config {

    public static final ConfigItemGroup mainGroup = new ConfigGroup();
    public static final ConfigItemGroup guiGroup = new GuiConfig();

    public static final List<ConfigItemGroup> configs = new ArrayList<>(Arrays.asList(mainGroup, guiGroup));

    public ClientConfig() {
        super(configs, new File(FabricLoader.getInstance().getConfigDir().toFile(), "sclient.json"), "sclient");
    }

    public static class ConfigGroup extends ConfigItemGroup {
        public static final ConfigItem<Boolean> fullBright = new ConfigItem<>("full_bright", false, "full_bright");
        public static final ConfigItem<Boolean> smoothCamera = new ConfigItem<>("smooth_camera", true, "smooth_camera");
        public static final ConfigItem<Boolean> fpsDisplay = new ConfigItem<>("fps_display", false, "fps_display");
        public static final ConfigItem<Boolean> cameraNoClip = new ConfigItem<>("camera_no_clip", false, "camera_no_clip");
        public static final ConfigItem<Boolean> highlightPlayers = new ConfigItem<>("highlight_players", false, "highlight_players");
        public static final ConfigItem<Boolean> nameTagF5 = new ConfigItem<>("name_tag_f5", false, "name_tag_f5");
        public static final ConfigItem<Boolean> clearWater = new ConfigItem<>("clear_water", false, "clear_water");
        public static final ConfigItem<Boolean> coordinateDisplay = new ConfigItem<>("coordinate_display", false, "coordinate_display");
        public static final ConfigItem<Boolean> renderHud = new ConfigItem<>("render_hud", true, "render_hud");
        public static final ConfigItem<Boolean> capeMod = new ConfigItem<>("cape_mod", true, "cape_mod");
        public static final ConfigItem<Double> nameTagScale = new ConfigItem<>("name_tag_scale", 1.0, "name_tag_scale");
        public static final ConfigItem<Double> zoomFOV = new ConfigItem<>("zoom_fov", 10.0, "zoom_fov");

        public ConfigGroup() {
            super(new ArrayList<>(Arrays.asList(fullBright, smoothCamera, fpsDisplay, cameraNoClip, highlightPlayers, nameTagF5, clearWater, coordinateDisplay, renderHud, nameTagScale, zoomFOV)), "config");
        }
    }

    public static class GuiConfig extends ConfigItemGroup {
        public static final NestedGroup fpsDisplay = new NestedGroup("fps_display");
        public static final NestedGroup coordinateDisplay = new NestedGroup("coordinate_display");
        public static final NestedGroup hudDisplay = new NestedGroup("hud_display");

        public GuiConfig() {
            super(new ArrayList<>(Arrays.asList(fpsDisplay, coordinateDisplay, hudDisplay)), "gui");
        }

        public static class NestedGroup extends ConfigItemGroup {

            public NestedGroup(String name) {
                super(new ArrayList<>(Arrays.asList(new ConfigItem<>("x_per", 0.0, "x_per"), new ConfigItem<>("y_per", 0.0, "y_per"))), name);
            }
        }
    }
}

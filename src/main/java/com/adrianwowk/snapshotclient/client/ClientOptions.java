package com.adrianwowk.snapshotclient.client;

import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.gui.hud.ModHud;
import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;

public class ClientOptions {

    public static final CyclingOption<Boolean> FULL_BRIGHT = CyclingOption.create("options.full_bright",
            (gameOptions) -> Mods.FULL_BRIGHT_MOD.enabled, (gameOptions, option, fullbright) -> Mods.FULL_BRIGHT_MOD.setEnabled(fullbright));

    public static final CyclingOption<Boolean> SMOOTH_CAMERA = CyclingOption.create("options.smooth_camera",
            (gameOptions) -> Mods.ZOOM_MOD.smoothCamera, (gameOptions, option, smoothCam) -> Mods.ZOOM_MOD.setSmoothCamera(smoothCam));

    public static final CyclingOption<Boolean> FPS_ON = CyclingOption.create("options.fps",
            (gameOptions) -> Mods.FPS_MOD.enabled, (gameOptions, option, fps) -> Mods.FPS_MOD.setEnabled(fps));

    public static final CyclingOption<Boolean> CAMERA_NO_CLIP = CyclingOption.create("options.no_clip",
            (gameOptions) -> Mods.PERSPECTIVE_MOD.cameraNoClip, (gameOptions, option, noClip) -> Mods.PERSPECTIVE_MOD.setNoClip(noClip));

    public static final CyclingOption<Boolean> HIGHLIGHT_PLAYERS = CyclingOption.create("options.highlight_players",
            (gameOptions) -> Mods.HIGHLIGHT_PLAYER_MOD.enabled, (gameOptions, option, highlight) -> Mods.HIGHLIGHT_PLAYER_MOD.setEnabled(highlight));

    public static final CyclingOption<Boolean> NAME_TAG_F5 = CyclingOption.create("options.name_tag_f5",
            (gameOptions) -> Mods.NAME_TAG_MOD.enabled, (gameOptions, option, nameTag) -> Mods.NAME_TAG_MOD.setEnabled(nameTag));

    public static final CyclingOption<Boolean> CLEAR_WATER = CyclingOption.create("options.clear_water",
            (gameOptions) -> Mods.CLEAR_WATER_MOD.enabled, (gameOptions, option, clear) -> Mods.CLEAR_WATER_MOD.setEnabled(clear));

    public static final CyclingOption<Boolean> RENDER_HUD = CyclingOption.create("options.render_hud",
            (gameOptions) -> ModHud.renderHud, (gameOptions, option, render) -> {
                ModHud.renderHud = render;
                ClientConfig.ConfigGroup.renderHud.setValue(render);
                SnapshotclientClient.CONFIG.saveConfigToFile();
            });

    public static final CyclingOption<Boolean> COORDINATES = CyclingOption.create("options.coordinates",
            (gameOptions) -> Mods.COORDINATE_MOD.enabled, (gameOptions, option, on) -> Mods.COORDINATE_MOD.setEnabled(on));

    public static final DoubleOption NAME_TAG_SCALE = new DoubleOption("options.name_tag_scale", 1.0D, 3.0D, 0.1F,
            (gameOptions) -> Mods.NAME_TAG_MOD.nameTagScale, (gameOptions, scale) -> Mods.NAME_TAG_MOD.setNameTagScale(scale), (gameOptions, option) -> Text.of(String.format("Name Tag Scale: %.01fx", Mods.NAME_TAG_MOD.nameTagScale)));

    public static final DoubleOption ZOOM_FOV = new DoubleOption("options.zoom_fov", 2.0D, 30.0D, 1.0F,
            (gameOptions) -> Mods.ZOOM_MOD.zoomFOV, (gameOptions, fov) -> Mods.ZOOM_MOD.setFov(fov), (gameOptions, option) -> Text.of(String.format("Zoom FOV: %.01f", Mods.ZOOM_MOD.zoomFOV)));

    public static final Option[] OPTIONS = new Option[] {ZOOM_FOV, FULL_BRIGHT, SMOOTH_CAMERA, FPS_ON, CAMERA_NO_CLIP, HIGHLIGHT_PLAYERS, NAME_TAG_F5, NAME_TAG_SCALE, CLEAR_WATER, RENDER_HUD, COORDINATES};
}

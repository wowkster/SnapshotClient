package com.adrianwowk.snapshotclient.client.mods;

import com.adrianwowk.snapshotclient.client.mods.key.FullBrightMod;
import com.adrianwowk.snapshotclient.client.mods.key.PerspectiveMod;
import com.adrianwowk.snapshotclient.client.mods.key.ZoomMod;
import com.adrianwowk.snapshotclient.client.mods.persistent.*;

public class Mods {
    public static Mod[] MODS;

    public static ZoomMod ZOOM_MOD;
    public static FullBrightMod FULL_BRIGHT_MOD;
    public static FPSMod FPS_MOD;
    public static PerspectiveMod PERSPECTIVE_MOD;
    public static HighlightPlayerMod HIGHLIGHT_PLAYER_MOD;
    public static NameTagMod NAME_TAG_MOD;
    public static ClearWaterMod CLEAR_WATER_MOD;
    public static CoordinateMod COORDINATE_MOD;
    public static CapeMod CAPE_MOD;

    public static void init(){

       ZOOM_MOD = new ZoomMod();
       FULL_BRIGHT_MOD = new FullBrightMod();
       FPS_MOD = new FPSMod();
       PERSPECTIVE_MOD = new PerspectiveMod();
       HIGHLIGHT_PLAYER_MOD = new HighlightPlayerMod();
       NAME_TAG_MOD = new NameTagMod();
       CLEAR_WATER_MOD = new ClearWaterMod();
       COORDINATE_MOD = new CoordinateMod();
       CAPE_MOD = new CapeMod();

       MODS = new Mod[] {ZOOM_MOD, FULL_BRIGHT_MOD, FPS_MOD, PERSPECTIVE_MOD, HIGHLIGHT_PLAYER_MOD, NAME_TAG_MOD, CLEAR_WATER_MOD, COORDINATE_MOD, CAPE_MOD};
    }
}

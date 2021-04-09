package com.adrianwowk.snapshotclient.client.mods.key;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.KeyMod;
import com.adrianwowk.snapshotclient.client.mods.Mods;
import net.minecraft.client.option.Perspective;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class PerspectiveMod extends KeyMod {

    public float cameraYaw;
    public float cameraPitch;
    public Perspective lastPerspective;
    public boolean cameraNoClip;

    public PerspectiveMod() {
        super(GLFW.GLFW_KEY_R, "perspective");
        cameraNoClip = ClientConfig.ConfigGroup.cameraNoClip.getValue();
    }

    @Override
    public void keyUp() {
        enabled = false;
        client.options.setPerspective(lastPerspective);
    }

    @Override
    public void keyDown() {
        enabled = true;
        lastPerspective = client.options.getPerspective();
        cameraPitch = client.player.pitch;
        cameraYaw = client.player.yaw;
    }

    @Override
    public Text getDesc() {
        return Text.of("§ePerspective:§f " + (Mods.PERSPECTIVE_MOD.enabled ? "On" : "Off") + " §7| §eNo Clip:§f " + (Mods.PERSPECTIVE_MOD.cameraNoClip ? "On" : "Off"));
    }

    public void setNoClip(boolean noClip) {
        this.cameraNoClip = noClip;
        ClientConfig.ConfigGroup.cameraNoClip.setValue(noClip);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }
}

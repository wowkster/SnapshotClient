package com.adrianwowk.snapshotclient.client.mods.key;

import com.adrianwowk.snapshotclient.client.SnapshotclientClient;
import com.adrianwowk.snapshotclient.client.config.ClientConfig;
import com.adrianwowk.snapshotclient.client.mods.KeyMod;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ZoomMod extends KeyMod {

    private double startFov;
    public double zoomFOV;
    public boolean smoothCamera;
    private double lastAddition = 0;

    public ZoomMod() {
        super(GLFW.GLFW_KEY_C, "zoom");
        zoomFOV = ClientConfig.ConfigGroup.zoomFOV.getValue();
        smoothCamera = ClientConfig.ConfigGroup.smoothCamera.getValue();
    }

    @Override
    public void keyUp() {
        client.options.fov = startFov;
        if (smoothCamera)
            client.options.smoothCameraEnabled = false;
        enabled = false;
    }

    @Override
    public void keyDown() {
        startFov = client.options.fov;
        client.options.fov = zoomFOV;
        if (smoothCamera)
            client.options.smoothCameraEnabled = true;
        enabled = true;
    }

    @Override
    public Text getDesc() {
        return Text.of("§eZoom:§f (FOV " + zoomFOV + ", Camera: " + (smoothCamera ? "Smooth" : "Default") + ")");
    }

    public void setFov(double fov) {
        zoomFOV = fov;
        ClientConfig.ConfigGroup.zoomFOV.setValue(fov);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }

    public void setSmoothCamera(boolean smoothCam) {
        this.smoothCamera = smoothCam;
        ClientConfig.ConfigGroup.smoothCamera.setValue(smoothCam);
        SnapshotclientClient.CONFIG.saveConfigToFile();
    }

    public void addFOV(double scrollAmount){
        double newFOV = client.options.fov;

        if (Math.abs(scrollAmount - lastAddition) == 1){
            newFOV += Math.abs(scrollAmount) / scrollAmount;
        } else {
            newFOV += scrollAmount;
        }

        if (!(newFOV > 30) && !(newFOV < 2)) {
            client.options.fov = newFOV;
        } else {
            System.out.println("Didn't allow change");
        }

        lastAddition = scrollAmount;
    }

}

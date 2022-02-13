package com.adrianwowk.snapshotclient.mixin;

import net.minecraft.client.model.ModelPart;

public interface PlayerEntityModelAccess {
    ModelPart getCustomCloak();

    void setCustomCloak(ModelPart mp);
}

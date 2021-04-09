package com.adrianwowk.snapshotclient.access;

import net.minecraft.client.model.ModelPart;

public interface PlayerEntityModelAccess {

    ModelPart getCustomCloak();

    void setCustomCloak(ModelPart mp);
}

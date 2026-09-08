package com.he.lastsongofelysian.client;

import com.he.lastsongofelysian.network.CocoonStageSyncPacket;

public final class ClientCocoonPacketHandler {

    private ClientCocoonPacketHandler() {
    }

    public static void handle(CocoonStageSyncPacket message) {
        ClientCocoonData.setCoreStage(message.getStage());
        ClientCocoonData.setReversed(message.isReversed());
    }
}

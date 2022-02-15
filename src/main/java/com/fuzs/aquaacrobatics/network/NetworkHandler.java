package com.fuzs.aquaacrobatics.network;

import com.fuzs.aquaacrobatics.network.message.PacketSendKey;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public NetworkHandler() {
    }

    public static int nextID() {
        return packetId++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(PacketSendKey.Handler.class, PacketSendKey.class, nextID(), Side.SERVER);
    }
}

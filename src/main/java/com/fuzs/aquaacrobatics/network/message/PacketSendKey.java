package com.fuzs.aquaacrobatics.network.message;

import com.fuzs.aquaacrobatics.entity.player.IPlayerResizeable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendKey implements IMessage {
    public enum KeybindPacket {
        UNKNOWN,
        TOGGLE_CRAWLING
    }
    private KeybindPacket keybind = KeybindPacket.UNKNOWN;

    @Override
    public void fromBytes(ByteBuf buf) {
        int idx = buf.readInt();
        if(idx >= KeybindPacket.values().length)
            keybind = KeybindPacket.UNKNOWN;
        else
            keybind = KeybindPacket.values()[idx];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(keybind.ordinal());
    }

    public PacketSendKey() {

    }

    public PacketSendKey(KeybindPacket keybind) {
        this.keybind = keybind;
    }

    public static class Handler implements IMessageHandler<PacketSendKey, IMessage> {
        @Override
        public IMessage onMessage(PacketSendKey message, MessageContext ctx) {
            // Always use a construct like this to actually handle your message. This ensures that
            // your 'handle' code is run on the main Minecraft thread. 'onMessage' itself
            // is called on the networking thread so it is not safe to do a lot of things
            // here.
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendKey message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            if(message.keybind == KeybindPacket.TOGGLE_CRAWLING) {
                IPlayerResizeable resizeable = (IPlayerResizeable)playerEntity;
                resizeable.setForcingCrawling(!resizeable.isForcingCrawling());
            }
        }
    }
}
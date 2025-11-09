package lv.id.bonne.arsnouveaucraftingterminal.network;


import com.hollingsworth.arsnouveau.common.network.AbstractPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class Networking
{
    public static void register(final RegisterPayloadHandlersEvent event)
    {
        // Sets the current network version
        final PayloadRegistrar reg = event.registrar("1");
        reg.playBidirectional(SetGuiSettingsPacket.TYPE,
            SetGuiSettingsPacket.CODEC,
            new DirectionalPayloadHandler<>(ClientMessageHandler::handleClient,
                (msg, ctx) -> msg.onServerReceived(ctx.player().getServer(), (ServerPlayer) ctx.player())));
    }


    private static class ClientMessageHandler
    {
        public static <T extends AbstractPacket> void handleClient(T message, IPayloadContext ctx)
        {
            Minecraft minecraft = Minecraft.getInstance();
            message.onClientReceived(minecraft, minecraft.player);
        }
    }


    public static void sendToPlayerClient(CustomPacketPayload msg, ServerPlayer player)
    {
        PacketDistributor.sendToPlayer(player, msg);
    }


    public static void sendToServer(CustomPacketPayload msg)
    {
        PacketDistributor.sendToServer(msg);
    }
}

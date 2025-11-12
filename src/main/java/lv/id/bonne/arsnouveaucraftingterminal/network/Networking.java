package lv.id.bonne.arsnouveaucraftingterminal.network;


import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.network.packets.ClientGuiSettingsPacket;
import lv.id.bonne.arsnouveaucraftingterminal.network.packets.HandShakePacket;
import lv.id.bonne.arsnouveaucraftingterminal.network.packets.ServerGuiSettingsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ArsNouveauCraftingTerminal.MODID)
public class Networking
{
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer player &&
            player.connection.hasChannel(HandShakePacket.TYPE))
        {
            // Send to client that mod is enabld on server
            PacketDistributor.sendToPlayer(player, new HandShakePacket());
        }
    }


    public static void register(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar reg = event.registrar("2").optional();

        // Register client-to-server packet
        reg.playToServer(ClientGuiSettingsPacket.TYPE,
            ClientGuiSettingsPacket.CODEC,
            (msg, ctx) -> msg.handle(ctx.player().getServer(), (ServerPlayer) ctx.player()));

        reg.playToClient(HandShakePacket.TYPE, HandShakePacket.STREAM_CODEC, HandShakePacket::handle);
    }


    public static void sendToPlayerClient(ServerGuiSettingsPacket msg, ServerPlayer player)
    {
        if (player.connection.hasChannel(msg.type()))
        {
            PacketDistributor.sendToPlayer(player, msg);
        }
    }
}
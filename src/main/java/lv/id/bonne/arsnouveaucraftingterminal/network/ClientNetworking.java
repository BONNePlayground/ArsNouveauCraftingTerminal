package lv.id.bonne.arsnouveaucraftingterminal.network;


import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.network.packets.ClientGuiSettingsPacket;
import lv.id.bonne.arsnouveaucraftingterminal.network.packets.ServerGuiSettingsPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@EventBusSubscriber(modid = ArsNouveauCraftingTerminal.MODID, value = Dist.CLIENT)
public class ClientNetworking
{
    @SubscribeEvent
    public static void onPlayerDisconnected(PlayerEvent.PlayerLoggedOutEvent event)
    {
        // Reset value
        ClientNetworking.SERVER_SIDE = false;
    }


    public static void register(final RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar reg = event.registrar("2").optional();

        reg.playToClient(ServerGuiSettingsPacket.TYPE,
            ServerGuiSettingsPacket.CODEC,
            (msg, ctx) ->
            {
                Minecraft minecraft = Minecraft.getInstance();
                msg.handle(minecraft, minecraft.player);
            });
    }


    public static void sendToServer(ClientGuiSettingsPacket msg)
    {
        PacketDistributor.sendToServer(msg);
    }


    public static boolean hasServerSide()
    {
        return SERVER_SIDE;
    }


    /**
     * Indicates if Mod is enabled on server side.
     */
    public static boolean SERVER_SIDE = false;
}
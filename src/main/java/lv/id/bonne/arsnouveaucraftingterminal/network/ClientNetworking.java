package lv.id.bonne.arsnouveaucraftingterminal.network;


import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.network.packets.ClientGuiSettingsPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;


@EventBusSubscriber(modid = ArsNouveauCraftingTerminal.MODID, value = Dist.CLIENT)
public class ClientNetworking
{
    @SubscribeEvent
    public static void onPlayerDisconnected(PlayerEvent.PlayerLoggedOutEvent event)
    {
        // Reset value
        ClientNetworking.SERVER_SIDE = false;
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
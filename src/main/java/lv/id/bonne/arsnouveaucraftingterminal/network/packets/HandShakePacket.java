package lv.id.bonne.arsnouveaucraftingterminal.network.packets;


import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.network.ClientNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * This is a handshake packet that indicates client that mod is installed on server side.
 */
public record HandShakePacket() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<HandShakePacket> TYPE =
        new CustomPacketPayload.Type<>(ArsNouveauCraftingTerminal.prefix("handshake"));

    public static final StreamCodec<FriendlyByteBuf, HandShakePacket> STREAM_CODEC =
        StreamCodec.unit(new HandShakePacket());

    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public static void handle(HandShakePacket msg, IPayloadContext context)
    {
        if (context.flow().isClientbound())
        {
            context.enqueueWork(() ->
            {
                ClientNetworking.SERVER_SIDE = true;
            });
        }
    }
}
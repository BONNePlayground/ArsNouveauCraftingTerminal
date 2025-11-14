package lv.id.bonne.arsnouveaucraftingterminal.network.packets;


import java.util.Optional;

import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.client.container.GuiSettings;
import lv.id.bonne.arsnouveaucraftingterminal.injectors.CraftingTerminalInjector;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;


/**
 * This is a packet send from client to server with new gui settings.
 */
public record ClientGuiSettingsPacket(GuiSettings settings) implements CustomPacketPayload
{
    public static final Type<ClientGuiSettingsPacket> TYPE =
        new Type<>(ArsNouveauCraftingTerminal.prefix("client_gui_settings"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientGuiSettingsPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.optional(GuiSettings.STREAM_CODEC),
        s -> Optional.ofNullable(s.settings),
        ClientGuiSettingsPacket::new);


    public ClientGuiSettingsPacket(Optional<GuiSettings> settings)
    {
        this(settings.orElse(null));
    }


    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    private void handle(MinecraftServer minecraftServer, ServerPlayer player)
    {
        if (player.containerMenu instanceof CraftingTerminalInjector terminalScreen)
        {
            terminalScreen.receiveSettings(settings);
        }
    }


    public static void handle(ClientGuiSettingsPacket msg, IPayloadContext context)
    {
        if (context.flow().isServerbound())
        {
            context.enqueueWork(() ->
            {
                msg.handle(context.player().getServer(), (ServerPlayer) context.player());
            });
        }
    }
}
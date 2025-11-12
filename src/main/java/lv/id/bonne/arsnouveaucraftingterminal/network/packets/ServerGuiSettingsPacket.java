package lv.id.bonne.arsnouveaucraftingterminal.network.packets;


import java.util.Optional;

import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.client.container.GuiSettings;
import lv.id.bonne.arsnouveaucraftingterminal.injectors.CraftingTerminalInjector;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;


/**
 * This is a packet send from server to client with new gui settings.
 */
public record ServerGuiSettingsPacket(GuiSettings settings) implements CustomPacketPayload
{
    public static final Type<ServerGuiSettingsPacket> TYPE =
        new Type<>(ArsNouveauCraftingTerminal.prefix("server_gui_settings"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerGuiSettingsPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.optional(GuiSettings.STREAM_CODEC),
        s -> Optional.ofNullable(s.settings),
        ServerGuiSettingsPacket::new);


    public ServerGuiSettingsPacket(Optional<GuiSettings> settings)
    {
        this(settings.orElse(null));
    }


    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    public void handle(Minecraft minecraft, Player player)
    {
        if (minecraft.screen instanceof CraftingTerminalInjector terminalScreen)
        {
            terminalScreen.receiveSettings(settings);
        }
    }
}
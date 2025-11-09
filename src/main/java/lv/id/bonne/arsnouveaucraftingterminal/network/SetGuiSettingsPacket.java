package lv.id.bonne.arsnouveaucraftingterminal.network;


import com.hollingsworth.arsnouveau.common.network.AbstractPacket;

import java.util.Optional;

import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.client.container.GuiSettings;
import lv.id.bonne.arsnouveaucraftingterminal.injectors.CraftingTerminalInjector;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;


public class SetGuiSettingsPacket extends AbstractPacket
{
    public static final Type<SetGuiSettingsPacket> TYPE = new Type<>(ArsNouveauCraftingTerminal.prefix("gui_settings"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetGuiSettingsPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.optional(GuiSettings.STREAM_CODEC),
        s -> Optional.ofNullable(s.settings),
        SetGuiSettingsPacket::new);

    public GuiSettings settings;


    public SetGuiSettingsPacket(Optional<GuiSettings> settings)
    {
        this(settings.orElse(null));
    }


    public SetGuiSettingsPacket(GuiSettings settings)
    {
        this.settings = settings;
    }


    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }


    @Override
    public void onClientReceived(Minecraft minecraft, Player player)
    {
        super.onClientReceived(minecraft, player);
        if (minecraft.screen instanceof CraftingTerminalInjector terminalScreen)
        {
            terminalScreen.receiveSettings(settings);
        }
    }


    @Override
    public void onServerReceived(MinecraftServer minecraftServer, ServerPlayer player)
    {
        super.onServerReceived(minecraftServer, player);
        if (player.containerMenu instanceof CraftingTerminalInjector terminalScreen)
        {
            terminalScreen.receiveSettings(settings);
        }
    }
}

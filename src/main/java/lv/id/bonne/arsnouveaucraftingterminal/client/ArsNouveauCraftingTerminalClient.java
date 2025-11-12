//
// Created by BONNe
// Copyright - 2025
//


package lv.id.bonne.arsnouveaucraftingterminal.client;


import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.network.ClientNetworking;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;


@Mod(value = ArsNouveauCraftingTerminal.MODID, dist = Dist.CLIENT)
public class ArsNouveauCraftingTerminalClient
{
    public ArsNouveauCraftingTerminalClient(IEventBus modEventBus)
    {
        modEventBus.addListener(ClientNetworking::register);
    }
}

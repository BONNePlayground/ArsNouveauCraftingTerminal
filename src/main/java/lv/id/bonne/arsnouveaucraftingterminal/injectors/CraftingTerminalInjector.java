//
// Created by BONNe
// Copyright - 2025
//


package lv.id.bonne.arsnouveaucraftingterminal.injectors;


import lv.id.bonne.arsnouveaucraftingterminal.client.container.GuiSettings;


public interface CraftingTerminalInjector
{
    void receiveSettings(GuiSettings settings);

    void setGuiSize(int guiSize);
}

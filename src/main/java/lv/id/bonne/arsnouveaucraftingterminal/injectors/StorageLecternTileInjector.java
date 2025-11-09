//
// Created by BONNe
// Copyright - 2025
//


package lv.id.bonne.arsnouveaucraftingterminal.injectors;


import lv.id.bonne.arsnouveaucraftingterminal.client.container.GuiSettings;


public interface StorageLecternTileInjector
{
    void setGuiSettings(GuiSettings settings);

    GuiSettings getGuiSettings();
}

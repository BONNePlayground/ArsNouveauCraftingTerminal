//
// Created by BONNe
// Copyright - 2025
//


package lv.id.bonne.arsnouveaucraftingterminal.mixin;


import com.hollingsworth.arsnouveau.client.container.CraftingTerminalMenu;
import com.hollingsworth.arsnouveau.client.container.SlotStorage;
import com.hollingsworth.arsnouveau.client.container.StorageTerminalMenu;
import com.hollingsworth.arsnouveau.common.block.tile.CraftingLecternTile;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import lv.id.bonne.arsnouveaucraftingterminal.client.container.GuiSettings;
import lv.id.bonne.arsnouveaucraftingterminal.injectors.CraftingTerminalInjector;
import lv.id.bonne.arsnouveaucraftingterminal.injectors.StorageLecternTileInjector;
import lv.id.bonne.arsnouveaucraftingterminal.network.Networking;
import lv.id.bonne.arsnouveaucraftingterminal.network.SetGuiSettingsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;


@Mixin(value = CraftingTerminalMenu.class, remap = false)
@Implements(@Interface(iface = CraftingTerminalInjector.class, prefix = "injector$", unique = true))
public abstract class CraftingTerminalMenuMixin extends StorageTerminalMenu
{
    @Shadow
    protected List<CraftingTerminalMenu.SlotCrafting> craftSlotList;


    @Shadow
    private CraftingTerminalMenu.ActiveResultSlot craftingResultSlot;


    public CraftingTerminalMenuMixin(MenuType<?> type, int id, Inventory inv)
    {
        super(type, id, inv);
    }


    /**
     * This injects ability to set gui settings into tile entity.
     */
    public void injector$receiveSettings(GuiSettings settings)
    {
        ((StorageLecternTileInjector) this.te).setGuiSettings(settings);
    }


    @Override
    public void broadcastChanges()
    {
        super.broadcastChanges();

        if (this.te == null) return;

        if (!this.anct$sentSettings)
        {
            GuiSettings guiSettings = ((StorageLecternTileInjector) this.te).getGuiSettings();

            Networking.sendToPlayerClient(new SetGuiSettingsPacket(guiSettings), (ServerPlayer) pinv.player);
            this.anct$sentSettings = true;
            this.anct$guiSize = guiSettings.guiSize();
        }
    }


    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lcom/hollingsworth/arsnouveau/common/block/tile/CraftingLecternTile;)V",
        at = @At(value = "INVOKE",
            target = "Lcom/hollingsworth/arsnouveau/client/container/CraftingTerminalMenu;init()V"))
    private void assignGuiSize(int id, Inventory inv, CraftingLecternTile te, CallbackInfo ci)
    {
        this.anct$guiSize = ((StorageLecternTileInjector) this.te).getGuiSettings().guiSize();
    }


    /**
     * @author BONNe
     * @reason I need to overwrite as I do not need to use super.addStorageSlots
     */
    @Overwrite
    public void addStorageSlots(boolean expanded)
    {
        int lines = 13;
        boolean shouldAdd = this.storageSlotList.isEmpty();

        int maxRows = (this.anct$guiSize + 1) * 3 + (expanded ? 4 : 0);

        for (int i = 0; i < lines; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                int index = i * 9 + j;

                if (shouldAdd)
                {
                    this.storageSlotList.add(new SlotStorage(this.te, index, 13 + j * 18, 21 + i * 18, i < maxRows));
                }
                else
                {
                    this.storageSlotList.get(index).show = i < maxRows;
                }
            }
        }


        if (this.craftSlotList != null)
        {
            for (CraftingTerminalMenu.SlotCrafting slot : this.craftSlotList)
            {
                slot.active = !expanded;
            }

            this.craftingResultSlot.active = !expanded;
        }

        if (this.craftSlotList != null)
        {
            int y = 70 + this.anct$guiSize * 54;

            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    ((SlotAccessor) this.craftSlotList.get(i * 3 + j)).setY(y + 18 + 1 + i * 18);
                }
            }

            ((SlotAccessor) this.craftingResultSlot).setY(y + 37);
        }

        if (this.playerSlotsStart != 0)
        {
            int y = 157 + anct$guiSize * 54;

            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 9; j++)
                {
                    ((SlotAccessor) this.slots.get(this.playerSlotsStart + 1 + i * 9 + j)).setY(y + i * 18);
                }
            }

            y += 58;

            for (int j = 0; j < 9; j++)
            {
                ((SlotAccessor) this.slots.get(this.playerSlotsStart + 1 + 27 + j)).setY(y);
            }
        }
    }


    public void injector$setGuiSize(int guiSize)
    {
        this.anct$guiSize = guiSize;
    }


    @Unique
    private boolean anct$sentSettings = false;

    @Unique
    private int anct$guiSize = 0;
}

//
// Created by BONNe
// Copyright - 2025
//


package lv.id.bonne.arsnouveaucraftingterminal.mixin;


import com.hollingsworth.arsnouveau.client.container.AbstractStorageTerminalScreen;
import com.hollingsworth.arsnouveau.client.container.CraftingTerminalMenu;
import com.hollingsworth.arsnouveau.client.container.CraftingTerminalScreen;
import com.hollingsworth.arsnouveau.client.container.IAutoFillTerminal;
import com.hollingsworth.arsnouveau.client.gui.buttons.StateButton;
import com.hollingsworth.arsnouveau.client.gui.buttons.StorageSettingsButton;
import org.checkerframework.checker.units.qual.A;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

import lv.id.bonne.arsnouveaucraftingterminal.ArsNouveauCraftingTerminal;
import lv.id.bonne.arsnouveaucraftingterminal.client.container.GuiSettings;
import lv.id.bonne.arsnouveaucraftingterminal.injectors.CraftingTerminalInjector;
import lv.id.bonne.arsnouveaucraftingterminal.network.Networking;
import lv.id.bonne.arsnouveaucraftingterminal.network.SetGuiSettingsPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


/**
 * This is main workforce mixin. It changes how CraftingTerminalScreen works.
 * If fixes some bugs, and introduces ability to expand screen size.
 */
@Mixin(value = CraftingTerminalScreen.class, remap = false)
@Implements(@Interface(iface = CraftingTerminalInjector.class, prefix = "injector$", unique = true))
public abstract class CraftingTerminalScreenMixin extends AbstractStorageTerminalScreen<CraftingTerminalMenu>
{
    public CraftingTerminalScreenMixin(CraftingTerminalMenu screenContainer,
        Inventory inv,
        Component titleIn)
    {
        super(screenContainer, inv, titleIn);
    }


    @Inject(method = "init", at = @At(value = "INVOKE",
        target = "Lcom/hollingsworth/arsnouveau/client/container/AbstractStorageTerminalScreen;init()V",
        shift = At.Shift.AFTER))
    private void injectGUISizeButtonInitialize(CallbackInfo ci)
    {
        this.anct$buttonGuiSize = this.addRenderableWidget(new StorageSettingsButton(this.leftPos - 17, this.topPos + 59, 22, 12, 66, 13, 0, ArsNouveauCraftingTerminal.prefix("textures/gui/gui_size.png"), b -> {
            if (++this.anct$guiSize >= 3) this.anct$guiSize = 0;
            Networking.sendToServer(new SetGuiSettingsPacket(new GuiSettings(this.anct$guiSize)));
            this.anct$reinitializeGUI();
        }));

        this.anct$buttonGuiSize.state = this.anct$guiSize;
    }


    @Inject(method = "render", at = @At(value = "INVOKE",
        target = "Lcom/hollingsworth/arsnouveau/client/container/AbstractStorageTerminalScreen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
        shift = At.Shift.AFTER))
    private void injectGUISizeButtonTooltip(GuiGraphics graphics,
        int mouseX,
        int mouseY,
        float partialTicks,
        CallbackInfo ci)
    {
        if (this.anct$buttonGuiSize.isHovered())
        {
            graphics.renderTooltip(this.font,
                Component.translatable("tooltip.ars_nouveau_crafting_terminal.size_" + this.anct$buttonGuiSize.state, IAutoFillTerminal.getHandlerName()),
                mouseX,
                mouseY);
        }
    }


    @Inject(method = "lambda$init$3", at = @At("TAIL"))
    private void injectGUISizeButtonOffset(int recipeButtonY, Button thisButton, CallbackInfo ci)
    {
        this.anct$buttonGuiSize.setX(this.leftPos - 18);
    }


    @Inject(method = "init", at = @At(value = "INVOKE",
        target = "Lcom/hollingsworth/arsnouveau/client/gui/buttons/StateButton;setX(I)V",
        ordinal = 2))
    private void injectGUISizeButtonOffset(CallbackInfo ci)
    {
        this.anct$buttonGuiSize.setX(this.leftPos - 18);
    }


// ---------------------------------------------------------------------
// Section: The GUI fix
// ---------------------------------------------------------------------


    @Inject(method = "init", at = @At(value = "INVOKE",
        target = "Lcom/hollingsworth/arsnouveau/client/container/AbstractStorageTerminalScreen;init()V",
        shift = At.Shift.AFTER))
    private void fixRowCount(CallbackInfo ci)
    {
        if (this.expanded)
        {
            this.rowCount += 4;
        }
    }


    @Redirect(method = "expandScreen", at = @At(value = "FIELD",
        target = "Lcom/hollingsworth/arsnouveau/client/container/CraftingTerminalScreen;rowCount:I",
        opcode = Opcodes.PUTFIELD))
    private void updateRowCountOnExpand(CraftingTerminalScreen instance, int value)
    {
        this.rowCount += 4;
    }


    @Redirect(method = "collapseScreen", at = @At(value = "FIELD",
        target = "Lcom/hollingsworth/arsnouveau/client/container/CraftingTerminalScreen;rowCount:I",
        opcode = Opcodes.PUTFIELD))
    private void updateRowCountOnCollapse(CraftingTerminalScreen instance, int value)
    {
        this.rowCount -= 4;
    }


// ---------------------------------------------------------------------
// Section: Custom Gui Implementation
// ---------------------------------------------------------------------


    /**
     * @author BONNe
     * @reason uses custom GUI images
     */
    @Overwrite
    public ResourceLocation getGui()
    {
        return switch (this.anct$guiSize)
        {
            case 2 -> anct$gui_large;
            case 1 -> anct$gui_medium;
            default -> anct$gui_small;
        };
    }


    @Inject(method = "init", at = @At(value = "INVOKE",
        target = "Lcom/hollingsworth/arsnouveau/client/container/AbstractStorageTerminalScreen;init()V"))
    private void injectCustomGuiSize(CallbackInfo ci)
    {
        switch (this.anct$guiSize)
        {
            case 2 -> {
                this.imageHeight = 357;
                this.rowCount = 9;
                this.anct$inventoryHeight = 181;
            }
            case 1 -> {
                this.imageHeight = 303;
                this.rowCount = 6;
                this.anct$inventoryHeight = 127;
            }
            default -> {
                this.imageHeight = 248;
                this.rowCount = 3;
                this.anct$inventoryHeight = 73;
            }
        }
    }


    @Inject(method = "init", at = @At(value = "INVOKE",
        target = "Lcom/hollingsworth/arsnouveau/client/container/AbstractStorageTerminalScreen;init()V",
        shift = At.Shift.AFTER))
    private void injectResetGuiSize(CallbackInfo ci)
    {
        // Reset image height
        this.imageHeight = 248;
    }


    @ModifyVariable(method = "init", at = @At("STORE"), ordinal = 0)
    private int changeRecipeButtonHeight(int recipeButtonHeight)
    {
        return this.topPos + this.anct$inventoryHeight + 17;
    }


    @ModifyVariable(method = "init", at = @At("STORE"), ordinal = 1)
    private int changeCollapseButtonHeight(int collapseButtonHeight)
    {
        return this.topPos + this.anct$inventoryHeight + 73 + 1;
    }


    @Inject(method = "onExpandedChanged", at = @At("HEAD"))
    private void injectGuiMenuSize(boolean expanded, CallbackInfo ci)
    {
        ((CraftingTerminalInjector) this.menu).setGuiSize(this.anct$guiSize);
    }


    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);

        graphics.blit(this.expanded ? anct$gui_expanded_inventory : anct$gui_crafting_inventory,
            this.leftPos, this.topPos + this.anct$inventoryHeight,
            0, 0,
            186, 73,
            186, 73);

        graphics.blit(anct$gui_player_inventory,
            this.leftPos, this.topPos + this.anct$inventoryHeight + 73,
            0, 0,
            186, 103,
            186, 103);
    }


    public void injector$receiveSettings(GuiSettings settings)
    {
        this.anct$guiSize = settings.guiSize();
        this.anct$reinitializeGUI();
        this.anct$buttonGuiSize.state = this.anct$guiSize;
    }


    public void injector$setGuiSize(int guiSize)
    {
        // Do nothing here.
    }


// ---------------------------------------------------------------------
// Section: Methods
// ---------------------------------------------------------------------


    @Unique
    private void anct$reinitializeGUI()
    {
        new ArrayList<>(this.children()).forEach(this::removeWidget);
        this.init();
    }


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------

    @Unique
    private static final ResourceLocation anct$gui_small =
        ArsNouveauCraftingTerminal.prefix("textures/gui/crafting_terminal_small.png");
    @Unique
    private static final ResourceLocation anct$gui_medium =
        ArsNouveauCraftingTerminal.prefix("textures/gui/crafting_terminal_medium.png");
    @Unique
    private static final ResourceLocation anct$gui_large =
        ArsNouveauCraftingTerminal.prefix("textures/gui/crafting_terminal_large.png");

    @Unique
    private static final ResourceLocation anct$gui_player_inventory =
        ArsNouveauCraftingTerminal.prefix("textures/gui/crafting_terminal_inventory_player.png");
    @Unique
    private static final ResourceLocation anct$gui_expanded_inventory =
        ArsNouveauCraftingTerminal.prefix("textures/gui/crafting_terminal_inventory_expanded.png");
    @Unique
    private static final ResourceLocation anct$gui_crafting_inventory =
        ArsNouveauCraftingTerminal.prefix("textures/gui/crafting_terminal_inventory_crafting.png");

    /**
     * Stores how large screen has been increased by different gui.
     */
    @Unique
    protected int anct$inventoryHeight;

    /**
     * The button that allows to change GUI size
     */
    @Unique
    protected StateButton anct$buttonGuiSize;

    /**
     * The current GUI size.
     */
    @Unique
    protected int anct$guiSize;
}

//
// Created by BONNe
// Copyright - 2025
//


package lv.id.bonne.arsnouveaucraftingterminal.mixin;


import com.hollingsworth.arsnouveau.client.container.AbstractStorageTerminalScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lv.id.bonne.arsnouveaucraftingterminal.network.ClientNetworking;


@Mixin(value = AbstractStorageTerminalScreen.class, remap = false)
public abstract class AbstractStorageTerminalScreenMixin
{
    @Shadow
    public abstract void scrollTo(float p_148329_1_);


    @Shadow
    protected float currentScroll;


    @Shadow
    protected int rowCount;


    /**
     * Fixes onExpand gui. It stuck into weird state because adding new rows breaks scroll if not reset.
     * There could be a fix to properly reset scroll, but it is complex and not worth.
     */
    @Inject(method = "onExpandedChanged", at = @At("TAIL"))
    private void fixExpand(boolean expanded, CallbackInfo ci)
    {
        this.scrollTo(this.currentScroll = 0);
    }


    @ModifyVariable(method = "scrollTo", at = @At("STORE"), ordinal = 0)
    private int fixRowCount(int value)
    {
        return this.rowCount;
    }


    @ModifyArg(method = "init", at = @At(value = "INVOKE",
        target = "Lcom/hollingsworth/arsnouveau/client/gui/buttons/StorageTabButton;<init>(IIIIIIIILnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/gui/components/Button$OnPress;)V"),
        index = 1)
    private int adjustOffsetOfTabs(int x)
    {
        if (!ClientNetworking.hasServerSide()) return x;

        return x + 15;
    }
}

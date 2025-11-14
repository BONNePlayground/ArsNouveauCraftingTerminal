//
// Created by BONNe
// Copyright - 2025
//


package lv.id.bonne.arsnouveaucraftingterminal.mixin;


import com.hollingsworth.arsnouveau.common.block.tile.ModdedTile;
import com.hollingsworth.arsnouveau.common.block.tile.StorageLecternTile;
import com.hollingsworth.arsnouveau.common.util.ANCodecs;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lv.id.bonne.arsnouveaucraftingterminal.client.container.GuiSettings;
import lv.id.bonne.arsnouveaucraftingterminal.injectors.StorageLecternTileInjector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


/**
 * This mixin injects new stuff into StorageLecternTile object
 */
@Mixin(value = StorageLecternTile.class, remap = false)
@Implements(@Interface(iface = StorageLecternTileInjector.class, prefix = "injector$", unique = true))
public abstract class StorageLecternTileMixin extends ModdedTile
{
    public StorageLecternTileMixin(BlockEntityType<?> tileEntityTypeIn,
        BlockPos pos,
        BlockState state)
    {
        super(tileEntityTypeIn, pos, state);
    }


    @Unique
    public GuiSettings anct$guiSettings = new GuiSettings();


    public void injector$setGuiSettings(GuiSettings settings)
    {
        this.anct$guiSettings = settings;
        this.updateBlock();
    }


    public GuiSettings injector$getGuiSettings()
    {
        return this.anct$guiSettings;
    }


    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void anct$saveAdditional(CompoundTag compound, HolderLookup.Provider pRegistries, CallbackInfo ci)
    {
        compound.put("anct_gui", ANCodecs.encode(GuiSettings.CODEC, this.anct$guiSettings));
    }


    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void anct$loadAdditional(CompoundTag compound, HolderLookup.Provider pRegistries, CallbackInfo ci)
    {
        if (compound.contains("anct_gui"))
        {
            this.anct$guiSettings = ANCodecs.decode(GuiSettings.CODEC, compound.getCompound("anct_gui"));
        }
    }
}

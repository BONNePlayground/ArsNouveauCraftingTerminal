package lv.id.bonne.arsnouveaucraftingterminal.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.inventory.Slot;


@Mixin(Slot.class)
public interface SlotAccessor {
    @Accessor("y")
    @Mutable
    void setY(int y);
}
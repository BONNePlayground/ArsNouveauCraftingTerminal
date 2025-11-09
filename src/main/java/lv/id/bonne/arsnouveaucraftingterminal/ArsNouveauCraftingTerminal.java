package lv.id.bonne.arsnouveaucraftingterminal;


import lv.id.bonne.arsnouveaucraftingterminal.network.Networking;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ArsNouveauCraftingTerminal.MODID)
public class ArsNouveauCraftingTerminal
{
    public ArsNouveauCraftingTerminal(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(Networking::register);
    }

    public static ResourceLocation prefix(String key)
    {
        return ResourceLocation.fromNamespaceAndPath(ArsNouveauCraftingTerminal.MODID, key);
    }

    public static final String MODID = "ars_nouveau_crafting_terminal";
}

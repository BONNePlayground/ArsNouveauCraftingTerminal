package lv.id.bonne.arsnouveaucraftingterminal.client.container;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;


public record GuiSettings(int guiSize) {

    public static final Codec<GuiSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("guiSize").forGetter(GuiSettings::guiSize)
    ).apply(instance, GuiSettings::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GuiSettings> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        GuiSettings::guiSize,
        GuiSettings::new
    );

    public GuiSettings() {
        this(0);
    }
}

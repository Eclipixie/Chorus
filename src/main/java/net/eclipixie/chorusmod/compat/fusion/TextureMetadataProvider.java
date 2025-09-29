package net.eclipixie.chorusmod.compat.fusion;

import com.supermartijn642.fusion.api.provider.FusionTextureMetadataProvider;
import com.supermartijn642.fusion.api.texture.DefaultTextureTypes;
import com.supermartijn642.fusion.api.texture.data.ConnectingTextureData;
import com.supermartijn642.fusion.api.texture.data.ConnectingTextureLayout;
import net.eclipixie.chorusmod.ChorusMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class TextureMetadataProvider extends FusionTextureMetadataProvider {
    public TextureMetadataProvider(PackOutput output) {
        super(ChorusMod.MOD_ID, output);
    }

    @Override
    protected void generate() {
        this.addTextureMetadata(
                ResourceLocation.fromNamespaceAndPath(ChorusMod.MOD_ID, "block/sculk_spring"),
                DefaultTextureTypes.CONNECTING,
                ConnectingTextureData.builder()
                        .layout(ConnectingTextureLayout.PIECED)
                        .build()
        );
    }
}

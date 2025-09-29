package net.eclipixie.chorusmod.compat.fusion;

import com.supermartijn642.fusion.api.model.DefaultModelTypes;
import com.supermartijn642.fusion.api.model.ModelInstance;
import com.supermartijn642.fusion.api.model.data.BaseModelData;
import com.supermartijn642.fusion.api.provider.FusionModelProvider;
import net.eclipixie.chorusmod.ChorusMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelProvider extends FusionModelProvider {
    public ModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(ChorusMod.MOD_ID, output, existingFileHelper);
    }

    @Override
    protected void generate() {
        this.addModel(
                ResourceLocation.fromNamespaceAndPath(ChorusMod.MOD_ID, "block/sculk_spring"),
                ModelInstance.of(
                        DefaultModelTypes.BASE,
                        BaseModelData.builder()
                                .parent(ResourceLocation.parse("block/cube_all"))
                                .texture("all", ResourceLocation.fromNamespaceAndPath(ChorusMod.MOD_ID, "block/sculk_spring"))
                                .build()));
    }
}

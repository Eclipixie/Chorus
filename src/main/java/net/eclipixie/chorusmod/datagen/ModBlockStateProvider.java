package net.eclipixie.chorusmod.datagen;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.block.custom.SculkCorruptedEndermanBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ChorusMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithRenderType(ModBlocks.VOIDGLASS.get(), "voidglass", "translucent");

        // Register blockstate with two variants
        getVariantBuilder(ModBlocks.SCULK_CORRUPTED_ENDERMAN.get())
                .forAllStates(state -> {
                    Boolean isTop = state.getValue(SculkCorruptedEndermanBlock.IS_TOP); // Assuming 'IS_ACTIVE' is your state property
                    String modelName = isTop ?
                            "sculk_corrupted_enderman_top_3d" :
                            "sculk_corrupted_enderman_3d";

                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc("block/" + modelName)))
                            .build();
                });
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void blockWithRenderType(Block block, String modelPath, String renderType) {
        // Create a model JSON with the render_type specified
        ModelFile model = models().withExistingParent(
                modelPath, "minecraft:block/cube_all")
                .texture("all", modLoc("block/" + modelPath))
                .renderType(renderType); // Set the render type (cutout, translucent, etc.)

        // Register the model with the Block
        simpleBlockWithItem(block, model);
    }

    private void blockWithCustomModel(Block block, String modelParent, String modelPath) {
        ModelFile model = generateCustomModel(modelParent, modelPath);

        simpleBlockWithItem(block, model);
    }

    private ModelFile generateCustomModel(String modelParent, String modelPath) {
        return models().withExistingParent(
                modelPath, modLoc("block/" + modelParent))
                .texture("all", modLoc("block/" + modelPath));
    }
}

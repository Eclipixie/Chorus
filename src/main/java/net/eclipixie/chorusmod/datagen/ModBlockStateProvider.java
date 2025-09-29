package net.eclipixie.chorusmod.datagen;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.block.custom.SculkCorruptedEndermanBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
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

        blockWithCustomModel(ModBlocks.SCULK_HARVESTER.get(), "sculk_harvester_3d", "sculk_harvester_2d");

        simpleBlockWithItem(ModBlocks.SCULK_SPRING.get(), new ModelFile.UncheckedModelFile(
                ResourceLocation.fromNamespaceAndPath(ChorusMod.MOD_ID, "block/sculk_spring")));

        // Register blockstate with two variants
        getVariantBuilder(ModBlocks.SCULK_CORRUPTED_ENDERMAN.get())
                .forAllStates(state -> {
                    Boolean isTop = state.getValue(SculkCorruptedEndermanBlock.IS_TOP); // Assuming 'IS_ACTIVE' is your state property
                    String modelName = isTop ?
                            "sculk_corrupted_enderman_top_3d" :
                            "sculk_corrupted_enderman_3d";

                    Direction facing = state.getValue(SculkCorruptedEndermanBlock.FACING);
                    int y = 0;

                    switch (facing) {
                        case NORTH -> y = 180;
                        case EAST ->  y = 270;
                        case SOUTH -> y =   0;
                        case WEST ->  y =  90;
                    }

                    return ConfiguredModel.builder()
                            .modelFile(models().withExistingParent(
                                    modelName + "_data", modLoc("block/" + modelName))
                                    .texture("all", modLoc("block/sculk_corrupted_enderman_2d"))
                                    .renderType("cutout")
                            )
                            .rotationY(y)
                            .build();
                });

        simpleBlockWithItem(ModBlocks.END_STONE_RAIL.get(), models().cubeColumn(
                "end_stone_rail",
                modLoc("block/end_stone_rail_side"),
                modLoc("block/end_stone_rail_end")));
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

package net.eclipixie.chorusmod.datagen;

import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        // ender shard
        oreBlasting(
                consumer,
                List.of(Items.ENDER_PEARL),
                RecipeCategory.MISC,
                ModItems.ENDER_SHARD.get(),
                0.75f,
                100,
                "ender_shard");

        //region voidglass
        ShapelessRecipeBuilder.shapeless(
                    RecipeCategory.BUILDING_BLOCKS,
                    ModBlocks.VOIDGLASS.get(), 1)
                .requires(ModItems.ENDER_SHARD.get(), 4)
                .requires(Items.GLASS)
                .unlockedBy("has_ender_shard", has(ModItems.ENDER_SHARD.get()))
                .save(consumer, getItemName(ModBlocks.VOIDGLASS.get()) + "_mono");

        ShapelessRecipeBuilder.shapeless(
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.VOIDGLASS.get(), 2)
                .requires(ModItems.ENDER_SHARD.get(), 7)
                .requires(Items.GLASS, 2)
                .unlockedBy("has_ender_shard", has(ModItems.ENDER_SHARD.get()))
                .save(consumer, getItemName(ModBlocks.VOIDGLASS.get()) + "_duo");
        //endregion

        // sculk harvester
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.SCULK_HARVESTER.get())
                .define('E', Ingredient.of(Blocks.END_STONE_BRICKS))
                .define('R', Ingredient.of(Blocks.END_ROD))
                .define('S', Ingredient.of(Items.ECHO_SHARD))
                .pattern("R R")
                .pattern("ESE")
                .pattern("RER")
                .unlockedBy(getHasName(Blocks.END_STONE_BRICKS), has(Blocks.END_STONE_BRICKS))
                .save(consumer);

        // smithing recipes go template, subject, material

        // voidburst spyglass
        customSmithingRecipe(
                ModBlocks.VOIDGLASS.get(),
                Items.SPYGLASS,
                Items.TWISTING_VINES,
                RecipeCategory.TOOLS,
                ModItems.VOIDBURST_SPYGLASS.get(),
                consumer);

        // soundsight spyglass
//        customSmithingRecipe(
//                Ingredient.of(ModItems.RESONATOR.get()),
//                Ingredient.of(Items.SPYGLASS),
//                Ingredient.of(Items.TWISTING_VINES),
//                RecipeCategory.TOOLS,
//                ModItems.VOIDBURST_SPYGLASS.get(),
//                consumer);
    }

    protected void customSmithingRecipe(ItemLike template, ItemLike base, ItemLike addition, RecipeCategory category, Item result, Consumer<FinishedRecipe> consumer) {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(template),
                        Ingredient.of(base),
                        Ingredient.of(addition),
                        category, result)
                .unlocks("has_"+getItemName(template), has(template))
                .save(consumer, getItemName(result) + "_smithing");
    }
}

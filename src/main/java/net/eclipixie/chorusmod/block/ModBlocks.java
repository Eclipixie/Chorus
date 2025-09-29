package net.eclipixie.chorusmod.block;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.block.custom.*;
import net.eclipixie.chorusmod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ChorusMod.MOD_ID);

    public static final RegistryObject<Block> VOIDGLASS = registerBlock("voidglass",
            () -> new VoidglassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion()));
    public static final RegistryObject<Block> SCULK_CORRUPTED_ENDERMAN = registerBlock("sculk_corrupted_enderman",
            () -> new SculkCorruptedEndermanBlock(BlockBehaviour.Properties.copy(Blocks.LECTERN).noOcclusion()));
    public static final RegistryObject<Block> SCULK_HARVESTER = registerBlock("sculk_harvester",
            () -> new SculkHarvesterBlock(BlockBehaviour.Properties.copy(Blocks.SCULK_CATALYST).noOcclusion()));
    public static final RegistryObject<Block> END_STONE_RAIL = registerBlock("end_stone_rail",
            () -> new EndStoneRail(BlockBehaviour.Properties.copy(Blocks.PEARLESCENT_FROGLIGHT)));
    public static final RegistryObject<Block> SCULK_SPRING =registerBlock("sculk_spring",
            () -> new SculkSpringBlock(BlockBehaviour.Properties.copy(Blocks.POWDER_SNOW)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

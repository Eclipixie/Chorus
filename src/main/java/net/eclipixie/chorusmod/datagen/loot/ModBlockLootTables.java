package net.eclipixie.chorusmod.datagen.loot;

import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.fluid.ModFluids;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
//        this.add(ModBlocks.SAPPHIRE_ORE.get(),
//                block -> createOreDrops(ModBlocks.SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get(), 2, 5, true));

        this.dropWhenSilkTouch(ModBlocks.VOIDGLASS.get());
        this.dropSelf(ModBlocks.SCULK_CORRUPTED_ENDERMAN.get());
        this.dropSelf(ModBlocks.SCULK_HARVESTER.get());
        this.add(ModBlocks.SCULK_SPRING.get(), noDrop());
        this.add(ModFluids.LIQUID_XP.block.get(), noDrop());

        this.dropSelf(ModBlocks.END_STONE_RAIL.get());
    }

    protected LootTable.Builder createOreDrops(Block pBlock, Item item, int min, int max, boolean fortune) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                )
        );
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}

package net.eclipixie.chorusmod.event;

import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.block.custom.SculkCorruptedEndermanBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockEvents {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        LevelAccessor level = event.getLevel();

        BlockState state = level.getBlockState(pos);

        // Check if the block being broken is the bottom part of the custom double block
        if (state.is(ModBlocks.SCULK_CORRUPTED_ENDERMAN.get())) {
            if (state.getValue(SculkCorruptedEndermanBlock.IS_TOP)) {
                level.destroyBlock(pos.below(), false);
            }
            else {
                level.destroyBlock(pos.above(), false);
            }
        }
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(BlockEvents::onBlockBreak);
    }
}

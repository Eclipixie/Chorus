package net.eclipixie.chorusmod.block.custom;

import net.eclipixie.chorusmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class VoidglassBlock extends GlassBlock {
    public VoidglassBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack useStack = pPlayer.getUseItem();

        if (useStack.is(ModItems.ENDER_SHARD.get())) {
            System.out.println("binding!");


        }

        return InteractionResult.SUCCESS;
    }
}

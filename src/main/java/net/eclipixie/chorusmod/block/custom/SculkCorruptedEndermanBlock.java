package net.eclipixie.chorusmod.block.custom;

import net.eclipixie.chorusmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.Nullable;

public class SculkCorruptedEndermanBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");

    public SculkCorruptedEndermanBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(IS_TOP);
        super.createBlockStateDefinition(pBuilder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        System.out.println("placing");
        BlockPos placingPos = pContext.getClickedPos();

        BlockState placingBelow = pContext.getLevel().getBlockState(placingPos.below());

        boolean isTop = placingBelow.is(ModBlocks.SCULK_CORRUPTED_ENDERMAN.get()) && placingBelow.getValue(IS_TOP);

        if (isTop) {
            return super.defaultBlockState().setValue(IS_TOP, true);
        }
        else {
            boolean placeable = pContext.getLevel().isEmptyBlock(placingPos.above());
            System.out.println(placeable);

            if (placeable) {
                pContext.getLevel().setBlock(placingPos.above(),
                        this.defaultBlockState().setValue(IS_TOP, true), 3);
                return this.defaultBlockState().setValue(IS_TOP, false);
            }
        }

        return null;
    }


}

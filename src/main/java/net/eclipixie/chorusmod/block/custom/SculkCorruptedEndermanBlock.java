package net.eclipixie.chorusmod.block.custom;

import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.block.entity.ModBlockEntities;
import net.eclipixie.chorusmod.block.entity.SculkCorruptedEndermanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SculkCorruptedEndermanBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty IS_TOP = BooleanProperty.create("is_top");

    public SculkCorruptedEndermanBlock(BlockBehaviour.Properties pProperties) { super(pProperties); }

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

        BlockState blockState = super.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());

        System.out.println(pContext.getHorizontalDirection());

        if (isTop) {
            return blockState.setValue(IS_TOP, true);
        }
        else {
            boolean placeable = pContext.getLevel().isEmptyBlock(placingPos.above());

            if (placeable) {
                pContext.getLevel().setBlock(placingPos.above(),
                        blockState.setValue(IS_TOP, true), 3);
                return blockState.setValue(IS_TOP, false);
            }
        }

        return null;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

            if (blockEntity instanceof SculkCorruptedEndermanBlockEntity) {
                ((SculkCorruptedEndermanBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);

            if (entity instanceof SculkCorruptedEndermanBlockEntity) {
                ((SculkCorruptedEndermanBlockEntity)entity).use(pState, pLevel, pPos, pPlayer, pHand, pHit);
            } else {
                throw new IllegalStateException("Container provider missing for SculkCorruptedEnderman");
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SculkCorruptedEndermanBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide())
            return null;

        return createTickerHelper(
                pBlockEntityType,
                ModBlockEntities.SCULK_CORRUPTED_ENDERMAN_ENTITY.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }
}

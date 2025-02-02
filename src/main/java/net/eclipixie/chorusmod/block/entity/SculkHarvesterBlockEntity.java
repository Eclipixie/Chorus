package net.eclipixie.chorusmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.Stack;

public class SculkHarvesterBlockEntity extends BlockEntity {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(2);

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private static final int HARVEST_DELAY = 10;
    private static final int HARVEST_RANGE = 7;

    protected final ContainerData containerData;
    private int progressRequirement = 5;
    private int progress;

    Stack<BlockPos> tendril = new Stack<>();

    public SculkHarvesterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SCULK_HARVESTER_ENTITY.get(), pPos, pBlockState);
        this.containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SculkHarvesterBlockEntity.this.progress;
                    case 1 -> SculkHarvesterBlockEntity.this.progressRequirement;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SculkHarvesterBlockEntity.this.progress = pValue;
                    case 1 -> SculkHarvesterBlockEntity.this.progressRequirement = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) return InteractionResult.sidedSuccess(!pLevel.isClientSide());

        ItemStack inputSlot = itemStackHandler.getStackInSlot(INPUT_SLOT);
        ItemStack outputSlot = itemStackHandler.getStackInSlot(OUTPUT_SLOT);

        ItemStack handStack = pPlayer.getItemInHand(pHand);

        System.out.println(inputSlot.getItem());

        if (handStack.is(Items.GLASS_BOTTLE)) { // if the player's holding an empty bottle
            int inputAmount = Math.min(handStack.getCount() + inputSlot.getCount(), handStack.getMaxStackSize());
            int remaining = handStack.getCount() - (inputAmount - inputSlot.getCount());

            itemStackHandler.setStackInSlot(INPUT_SLOT, new ItemStack(Items.GLASS_BOTTLE, inputAmount));
            handStack.setCount(remaining);
            pPlayer.setItemInHand(pHand, handStack);
        } else if (handStack.isEmpty()) { // if the player's hand is empty
            pPlayer.setItemInHand(pHand, outputSlot);
            itemStackHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
        }
        else return InteractionResult.SUCCESS; // otherwise assume the player wants to do smth else

        return InteractionResult.SUCCESS;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState, SculkHarvesterBlockEntity entity) {
        if (pLevel.isClientSide()) return; // quit if we're client side

        if (tendril.isEmpty()) { // add sculk block if tendril is empty
            if (pLevel.getBlockState(pPos.below()).is(Blocks.SCULK)) tendril.push(pPos.below());
            return;
        }

        // check if tendril is broken anywhere
        for (int i = 0; i < tendril.size(); i++) {
            if (!pLevel.getBlockState(tendril.get(i)).is(Blocks.SCULK)) {
                tendril.subList(i, tendril.size()).clear();
                break;
            }
        }

        BlockPos tendrilEnd = tendril.lastElement();

        boolean found = false;

        if (tendril.size() < HARVEST_RANGE) {
            // scannable blocks
            BlockPos[] positionCandidates = {
                    tendrilEnd.above(), tendrilEnd.below(),
                    tendrilEnd.north(), tendrilEnd.south(),
                    tendrilEnd.east(),  tendrilEnd.west()
            };

            for (BlockPos positionCandidate : positionCandidates) {
                // if it's a part of the tendril already, discard
                if (tendril.contains(positionCandidate)) continue;
                // if it's not sculk, discard
                if (!pLevel.getBlockState(positionCandidate).is(Blocks.SCULK)) continue;

                tendril.push(positionCandidate);
                found = true;
            }
        }

        // if the tendril is extended or it is not time to harvest, leave
        if (found || pLevel.getGameTime() % HARVEST_DELAY != 0) return;

        // time to harvest
        if (progress < progressRequirement) {
            progress++;
            pLevel.setBlock(tendril.pop(), Blocks.AIR.defaultBlockState(), 3);
        }

        if (progress < progressRequirement) return;

        if (!hasRecipe()) return;

        ItemStack outputStack = itemStackHandler.getStackInSlot(OUTPUT_SLOT);

        if (outputStack.isEmpty()) completeCraft();
        else if (outputStack.getCount() < outputStack.getMaxStackSize())
            completeCraft();
    }

    protected boolean hasRecipe() {
        return itemStackHandler.getStackInSlot(INPUT_SLOT).is(Items.GLASS_BOTTLE);
    }

    protected void completeCraft() {
        progress -= progressRequirement;
        itemStackHandler.extractItem(INPUT_SLOT, 1, false);
        itemStackHandler.setStackInSlot(OUTPUT_SLOT,
                new ItemStack(Items.EXPERIENCE_BOTTLE, itemStackHandler.getStackInSlot(OUTPUT_SLOT).getCount() + 1));
    }
}

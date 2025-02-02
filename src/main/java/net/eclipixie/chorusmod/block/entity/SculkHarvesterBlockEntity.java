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
        if (pLevel.isClientSide()) return InteractionResult.PASS;

        ItemStack outputSlot = itemStackHandler.getStackInSlot(OUTPUT_SLOT);
        if (outputSlot.isEmpty()) return InteractionResult.PASS;

        pPlayer.getInventory().add(outputSlot);
        itemStackHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);

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

        System.out.println(found);
        // if the tendril is extended or it is not time to harvest, leave
        if (found || pLevel.getGameTime() % HARVEST_DELAY != 0) return;
        System.out.println("harvesting");

        // time to harvest
        progress++;
        pLevel.setBlock(tendril.pop(), Blocks.AIR.defaultBlockState(), 3);

        if (progress < progressRequirement) return;

        if (!hasRecipe()) return;

        ItemStack outputStack = itemStackHandler.getStackInSlot(OUTPUT_SLOT);

        if (outputStack.isEmpty() ||
                (outputStack.is(Items.EXPERIENCE_BOTTLE) && outputStack.getCount() < outputStack.getMaxStackSize())) {
            itemStackHandler.extractItem(INPUT_SLOT, 1, false);
            itemStackHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(Items.EXPERIENCE_BOTTLE, outputStack.getCount() + 1));
        }
    }

    protected boolean hasRecipe() {
        return itemStackHandler.getStackInSlot(INPUT_SLOT).is(Items.GLASS_BOTTLE);
    }
}

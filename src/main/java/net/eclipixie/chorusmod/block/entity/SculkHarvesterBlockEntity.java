package net.eclipixie.chorusmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Stack;

public class SculkHarvesterBlockEntity extends BlockEntity implements WorldlyContainer {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            if(!level.isClientSide())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    };

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private static final int HARVEST_DELAY = 10;
    private static final int HARVEST_RANGE = 32;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap);
    }

    @Override
    public void onLoad() {
        lazyItemHandler = LazyOptional.of(
                () -> itemStackHandler
        );
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        lazyItemHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemStackHandler.serializeNBT());
        pTag.putInt("sculk_harvester.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("sculk_harvester.progress");

        super.load(pTag);
    }

    public void drops() {
        SimpleContainer container = new SimpleContainer(itemStackHandler.getSlots());

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            container.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, container);
    }

    public ItemStack getRenderInputStack() {
        if(itemStackHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            return itemStackHandler.getStackInSlot(INPUT_SLOT);
        } else {
            return itemStackHandler.getStackInSlot(OUTPUT_SLOT);
        }
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) return InteractionResult.sidedSuccess(!pLevel.isClientSide());

        ItemStack inputSlot = itemStackHandler.getStackInSlot(INPUT_SLOT);
        ItemStack outputSlot = itemStackHandler.getStackInSlot(OUTPUT_SLOT);

        ItemStack handStack = pPlayer.getItemInHand(pHand);

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
            BlockPos newSculkBlock = getAdjacentSculkBlock(pPos, pPos, pLevel);

            if (newSculkBlock != null) tendril.push(newSculkBlock);

            return;
        }

        // check if tendril is broken anywhere
        for (int i = 0; i < tendril.size(); i++) {
            if (!pLevel.getBlockState(tendril.get(i)).is(Blocks.SCULK)) {
                tendril.subList(i, tendril.size()).clear();
                break;
            }
        }

        if (tendril.isEmpty()) return;

        BlockPos tendrilEnd = tendril.lastElement();

        boolean found = false;

        if (tendril.size() < HARVEST_RANGE) {
            BlockPos newSculkBlock = getAdjacentSculkBlock(tendrilEnd, pPos, pLevel);
            if (newSculkBlock != null) {
                found = true;
                tendril.push(newSculkBlock);
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

    protected @Nullable BlockPos getAdjacentSculkBlock(BlockPos pos, BlockPos source, Level level) {
        BlockPos found = null;

        // scannable blocks
        List<BlockPos> positionCandidates = new java.util.ArrayList<>(List.of(
                pos.north(), pos.south(),
                pos.above(), pos.below(),
                pos.east(), pos.west()
        ));

        positionCandidates.sort(
                (o1, o2) -> (int) (o1.distSqr(source) - o2.distSqr(source)));

        for (BlockPos positionCandidate : positionCandidates) {
            // if it's a part of the tendril already, discard
            if (tendril.contains(positionCandidate)) continue;
            // if it's not sculk, discard
            if (!level.getBlockState(positionCandidate).is(Blocks.SCULK)) continue;

            tendril.push(positionCandidate);
            found = positionCandidate;

//            System.out.println(mags[0] + " " + mags[1] + " " + mags[2]);
            System.out.println(positionCandidates.size());
        }

        return found;
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

    //region inventory management
    @Override
    public int getContainerSize() {
        return itemStackHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return itemStackHandler.getStackInSlot(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack stack = itemStackHandler.getStackInSlot(pSlot);
        int remaining = Math.max(stack.getCount() - pAmount, 0);

        ItemStack newStack = new ItemStack(
                stack.getItem(),
                stack.getCount() - remaining);

        stack.setCount(remaining);
        if (stack.getCount() == 0)
            stack = ItemStack.EMPTY;

        itemStackHandler.setStackInSlot(pSlot, stack);

        return newStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack stack = itemStackHandler.getStackInSlot(pSlot);

        itemStackHandler.setStackInSlot(pSlot, ItemStack.EMPTY);

        return stack;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        itemStackHandler.setStackInSlot(pSlot, pStack);
    }

    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemStackHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
    //endregion

    //region side-specific
    @Override
    public boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack) {
        return pIndex == OUTPUT_SLOT;
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        if (pSide == Direction.DOWN) return new int[]{ OUTPUT_SLOT };
        else return new int[]{ INPUT_SLOT };
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return this.canPlaceItem(pIndex, pItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return pDirection == Direction.DOWN && pIndex == OUTPUT_SLOT;
    }
    //endregion

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

    @Override
    public @NotNull CompoundTag getUpdateTag() { return saveWithoutMetadata(); }
}

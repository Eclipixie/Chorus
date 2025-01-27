package net.eclipixie.chorusmod.block.entity;

import net.eclipixie.chorusmod.block.custom.SculkCorruptedEndermanBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.*;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SculkCorruptedEndermanBlockEntity extends BlockEntity implements Container {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1);

    public static final float PLAYER_RANGE = 2.0f;
    public static final float XP_ORB_RANGE = 7.0f;

    public static final int XP_DRAIN_RATE = 5;
    public static final int XP_DRAIN_DELAY = 5;

    private static final int OUTPUT_SLOT = 0;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData containerData;
    private int progressRequirement = 70;
    private int progress;

    public SculkCorruptedEndermanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SCULK_CORRUPTED_ENDERMAN_ENTITY.get(), pPos, pBlockState);
        this.containerData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SculkCorruptedEndermanBlockEntity.this.progress;
                    case 1 -> SculkCorruptedEndermanBlockEntity.this.progressRequirement;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SculkCorruptedEndermanBlockEntity.this.progress = pValue;
                    case 1 -> SculkCorruptedEndermanBlockEntity.this.progressRequirement = pValue;
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
        pTag.putInt("sculk_corrupted_enderman.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        itemStackHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("sculk_corrupted_enderman.progress");

        super.load(pTag);
    }

    public void drops() {
        SimpleContainer container = new SimpleContainer(itemStackHandler.getSlots());

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            container.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, container);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack outputStack = itemStackHandler.getStackInSlot(OUTPUT_SLOT);

        // if the output is not empty
        if (!outputStack.isEmpty()) {
            // if the player has an empty hand
            if (pPlayer.getItemInHand(pHand).isEmpty()) {
                pPlayer.setItemInHand(pHand, outputStack);
                itemStackHandler.setStackInSlot(OUTPUT_SLOT, ItemStack.EMPTY);
            }
            else return InteractionResult.PASS;
        }
        else return InteractionResult.PASS;
        return InteractionResult.PASS;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState, SculkCorruptedEndermanBlockEntity entity) {
        if (pState.getValue(SculkCorruptedEndermanBlock.IS_TOP))
            return;

        // ticking logic
        // so basically checking for nearby players/xp orbs

        if (pLevel.getGameTime() % XP_DRAIN_DELAY == 0) {
            AABB orbBoundArea = AABB.ofSize(pPos.getCenter(), XP_ORB_RANGE, XP_ORB_RANGE, XP_ORB_RANGE);
            AABB playerBoundArea = AABB.ofSize(pPos.getCenter(), PLAYER_RANGE, PLAYER_RANGE, PLAYER_RANGE);

            List<ExperienceOrb> orbs = pLevel.getEntitiesOfClass(ExperienceOrb.class, orbBoundArea);
            List<Player> players = pLevel.getEntitiesOfClass(Player.class, playerBoundArea);

            for (ExperienceOrb orb : orbs) {
                int xp = Math.min(orb.getValue(), XP_DRAIN_RATE);

                entity.progress += xp;
                orb.value = orb.getValue() - xp;

                if (orb.value <= 0)
                    orb.kill();
            }

            for (Player player : players) {
                if (player.totalExperience > 0) {
                    int xp = Math.min(player.totalExperience, XP_DRAIN_RATE);

                    entity.progress += xp;
                    player.totalExperience -= xp;
                }
            }
        }

        ItemStack outputStack = entity.itemStackHandler.getStackInSlot(OUTPUT_SLOT);

        if (progress >= progressRequirement && outputStack.getCount() < outputStack.getItem().getMaxStackSize(outputStack)) {
            if (outputStack.isEmpty())
                outputStack = new ItemStack(Items.ENDER_PEARL, 1);
            else
                outputStack.setCount(outputStack.getCount() + 1);

            entity.itemStackHandler.setStackInSlot(OUTPUT_SLOT, outputStack);
            progress -= progressRequirement;
        }
    }

    @Override
    public int getContainerSize() {
        return itemStackHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty())
                return true;
        }

        return false;
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
}

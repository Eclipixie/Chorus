package net.eclipixie.chorusmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SculkCorruptedEndermanBlockEntity extends BlockEntity {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1);

    public static final float PLAYER_RANGE = 1.0f;
    public static final float XP_ORB_RANGE = 5.0f;
    public static final float DEATH_RANGE = 5.0f;

    private static final int OUTPUT_SLOT = 0;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData containerData;
    private int progressRequirement = 100;
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

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        // ticking logic
        // so basically checking for nearby players/xp orbs
    }
}

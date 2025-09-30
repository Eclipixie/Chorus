package net.eclipixie.chorusmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class SculkXPBlockEntity extends BlockEntity {
    protected final FluidTank liquidXPTank = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            SculkXPBlockEntity.this.sendUpdate();
        }
    };

    private LazyOptional<IFluidHandler> liquidXPHandler = LazyOptional.empty();

    public SculkXPBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return liquidXPHandler.cast();
        }

        return super.getCapability(cap);
    }

    protected void sendUpdate() {
        setChanged();
        assert level != null;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    @Override
    public void onLoad() {
        liquidXPHandler = LazyOptional.of(
                () -> liquidXPTank
        );
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        liquidXPHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("liquidXPInventory", liquidXPTank.writeToNBT(new CompoundTag()));

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        liquidXPTank.readFromNBT(pTag.getCompound("liquidXPInventory"));

        super.load(pTag);
    }
}

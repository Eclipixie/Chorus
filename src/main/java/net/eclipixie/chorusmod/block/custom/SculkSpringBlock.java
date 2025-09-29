package net.eclipixie.chorusmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class SculkSpringBlock extends PowderSnowBlock {
    protected static final VoxelShape COLLISION_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public SculkSpringBlock(Properties pProperties) {
        super(pProperties);
    }

    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return COLLISION_SHAPE;
    }

    @ParametersAreNonnullByDefault
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!(pEntity instanceof LivingEntity) || pEntity.getFeetBlockState().is(this)) {
            pEntity.makeStuckInBlock(pState, new Vec3(0.9F, 1.5D, 0.9F));
        }

        if (!pLevel.isClientSide) {
            pEntity.setSharedFlagOnFire(false);

            if (pEntity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1, 0, false, true));
            }
        }
    }
}

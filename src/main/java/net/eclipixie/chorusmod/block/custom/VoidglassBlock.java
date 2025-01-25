package net.eclipixie.chorusmod.block.custom;

import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.util.Teleports;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class VoidglassBlock extends Block {
    public VoidglassBlock(Properties pProperties) {
        super(pProperties);
    }

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        Direction face = pHit.getDirection();

        float scalar = 0;

        float x = -face.getNormal().getX();
        float y = -face.getNormal().getY();
        float z = -face.getNormal().getZ();

        int lengthLim = 8;

        BlockPos probePos = pPos;

        while (pLevel.getBlockState(probePos).is(ModBlocks.VOIDGLASS.get()) && scalar < lengthLim) {
            probePos = probePos.offset((int)x, (int)y, (int)z);

            scalar++;
        }

        scalar = scalar * 8 - 7;

        x *= scalar;
        y *= scalar;
        z *= scalar;

        x += pPos.getX();
        y += pPos.getY();
        z += pPos.getZ();

        if (pLevel.getBlockState(new BlockPos((int) x, ((int) y) - 1, (int) z)).is(Blocks.AIR)) {
            y--;
        }

        x += 0.5f;
        z += 0.5f;

        Teleports.VoidburstTeleport(pLevel, pPlayer, new Vec3(x, y, z));

        return InteractionResult.SUCCESS;
    }
}

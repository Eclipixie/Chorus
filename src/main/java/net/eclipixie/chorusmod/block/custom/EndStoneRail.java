package net.eclipixie.chorusmod.block.custom;

import net.minecraft.world.level.block.RotatedPillarBlock;

public class EndStoneRail extends RotatedPillarBlock {
    public static final double ACCEL_SCALAR = 1;

    public EndStoneRail(Properties pProperties) {
        super(pProperties);
    }

//    public static Vec3 towardsTarget(Vec3 pos, BlockState state, Entity entity) {
//        Vec3 tossTarget = Vec3.atLowerCornerOf(state.getValue(EndStoneRail.FACING).getNormal());
//
//        Vec3 dir = entity.position().subtract(pos);
//
//        double max = Math.max(Math.max(
//                Math.abs(dir.x),
//                Math.abs(dir.y)),
//                Math.abs(dir.z));
//
//        Vec3 norm = new Vec3(0, 0, 0);
//
//        if (Math.abs(dir.x) == max) norm = new Vec3(dir.x, 0, 0);
//        if (Math.abs(dir.y) == max) norm = new Vec3(0, dir.y, 0);
//        if (Math.abs(dir.z) == max) norm = new Vec3(0, 0, dir.z);
//
//        tossTarget = tossTarget.add(norm.normalize().scale(1.5));
//
//        return pos.add(tossTarget).subtract(
//                entity.position()).normalize();
//    }
}

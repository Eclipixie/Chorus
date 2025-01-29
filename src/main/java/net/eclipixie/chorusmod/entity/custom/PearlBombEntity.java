package net.eclipixie.chorusmod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PearlBombEntity extends Entity {
    private static final int FUSE = 60;
    private static final int EXPLOSION_RANGE = 5;

    public static final float SPIN_SPEED = 10.0f;
    public static final float SPIN_ACCEL = 1.2f;

    public static final double MOVE_SPEED = 0.025;

    public PearlBombEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() { }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) { }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) { }

    @Override
    public void tick() {
        Level level = this.level();
        if (level.isClientSide) return;

        this.move(MoverType.SELF, new Vec3(0, MOVE_SPEED, 0));

        if (this.tickCount >= FUSE) {
            level.explode(this,
                    this.position().x, this.position().y, this.position().z,
                    EXPLOSION_RANGE, true, Level.ExplosionInteraction.NONE);

            this.remove(RemovalReason.DISCARDED);
        }
    }
}

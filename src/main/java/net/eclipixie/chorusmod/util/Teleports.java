package net.eclipixie.chorusmod.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;

public class Teleports {
    public static void RandomTeleport(Level pLevel, LivingEntity pEntityLiving, double range) {
        double d0 = pEntityLiving.getX();
        double d1 = pEntityLiving.getY();
        double d2 = pEntityLiving.getZ();

        for(int i = 0; i < 16; ++i) {
            double d3 = pEntityLiving.getX() + (pEntityLiving.getRandom().nextDouble() - 0.5) * range;
            double d4 = Mth.clamp(pEntityLiving.getY() + (double)(pEntityLiving.getRandom().nextInt(16) - 8), (double)pLevel.getMinBuildHeight(), (double)(pLevel.getMinBuildHeight() + ((ServerLevel)pLevel).getLogicalHeight() - 1));
            double d5 = pEntityLiving.getZ() + (pEntityLiving.getRandom().nextDouble() - 0.5) * range;
            if (pEntityLiving.isPassenger()) {
                pEntityLiving.stopRiding();
            }

            Vec3 vec3 = pEntityLiving.position();
            pLevel.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(pEntityLiving));
            EntityTeleportEvent.ChorusFruit event = ForgeEventFactory.onChorusFruitTeleport(pEntityLiving, d3, d4, d5);
            if (event.isCanceled()) {
                return;
            }

            if (pEntityLiving.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                SoundEvent soundevent = pEntityLiving instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                pLevel.playSound((Player)null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                pEntityLiving.playSound(soundevent, 1.0F, 1.0F);
                break;
            }
        }
    }

    public static void RandomTeleport(Level pLevel, LivingEntity pLivingEntity) {
        RandomTeleport(pLevel, pLivingEntity, 16.0);
    }

    public static void VoidburstTeleport(Level pLevel, LivingEntity pLivingEntity, Vec3 pos) {
        if (pLivingEntity.isPassenger()) {
            pLivingEntity.stopRiding();
        }

        pLevel.gameEvent(GameEvent.TELEPORT, pLivingEntity.position(), GameEvent.Context.of(pLivingEntity));
        EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(pLivingEntity, pos.x, pos.y, pos.z);
        if (event.isCanceled()) { return; }

        pLivingEntity.teleportTo(pos.x, pos.y, pos.z);
//        SoundEvent soundevent = SoundEvents.ENDERMAN_TELEPORT;
//
//        pLevel.playSound(
//                pLivingEntity,
//                new BlockPos((int) pos.x, (int) pos.y, (int) pos.z),
//                soundevent,
//                SoundSource.NEUTRAL,
//                5.0f, 1.0f
//        );

        pLevel.explode(
                pLivingEntity,
                pos.x, pos.y, pos.z,
                5.0f, false,
                Level.ExplosionInteraction.NONE
        );
    }

    public static void VoidburstTeleport(Level pLevel, LivingEntity pLivingEntity, double x, double y, double z) {
        VoidburstTeleport(pLevel, pLivingEntity, new Vec3(x, y, z));
    }
}

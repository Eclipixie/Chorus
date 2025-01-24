package net.eclipixie.chorusmod.util;

import net.eclipixie.chorusmod.mobeffects.ModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import java.util.List;

public class Teleports {
    public static int voidburstInstabilityDuration = 100;
    public static float voidburstExplosionRadius = 2.0f;

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
        // dunno why i need to do this
        if (!(pLevel instanceof ServerLevel)) {
            System.out.println("Weird voidburst edge case here");
            return;
        }

        if (pLivingEntity.isPassenger()) {
            pLivingEntity.stopRiding();
        }

        pLevel.gameEvent(GameEvent.TELEPORT, pLivingEntity.position(), GameEvent.Context.of(pLivingEntity));
        EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(pLivingEntity, pos.x, pos.y, pos.z);
        if (event.isCanceled()) { return; }

        pLivingEntity.teleportTo(pos.x, pos.y, pos.z);

        boolean unstable = pLivingEntity.getEffect(ModMobEffects.VOID_INSTABILITY.get()) != null;

        if (unstable) {
            // status effect
            int amplifier = pLivingEntity.getEffect(ModMobEffects.VOID_INSTABILITY.get()).getAmplifier();
            float radius = voidburstExplosionRadius + amplifier;

            pLivingEntity.addEffect(new MobEffectInstance(
                    ModMobEffects.VOID_INSTABILITY.get(),
                    voidburstInstabilityDuration, amplifier + 1));

            // explode
            Level.ExplosionInteraction interaction = amplifier == 0 ?
                    Level.ExplosionInteraction.NONE :
                    Level.ExplosionInteraction.MOB;

            Entity entity = amplifier == 0 ? pLivingEntity : (Entity) null;

            pLevel.explode(
                    entity,
                    pos.x, pos.y, pos.z,
                    radius, false,
                    interaction
            );

            // teleportation area (do after explosion so that teleported entities take damage)
            AABB bb = new AABB(
                    pos.x - radius, pos.y - radius, pos.z - radius,
                    pos.x + radius, pos.y + radius, pos.z + radius);

            List<LivingEntity> entities = pLevel.getEntitiesOfClass(LivingEntity.class, bb);

            for (int i = 0; i < entities.size(); i++) {
                LivingEntity teleportingEntity = entities.get(i);

                if (teleportingEntity == pLivingEntity) continue;

                RandomTeleport(pLevel, teleportingEntity);
            }
        }
        else {
            pLivingEntity.addEffect(new MobEffectInstance(
                    ModMobEffects.VOID_INSTABILITY.get(),
                    voidburstInstabilityDuration, 0));
        }
    }

    public static void VoidburstTeleport(Level pLevel, LivingEntity pLivingEntity, double x, double y, double z) {
        VoidburstTeleport(pLevel, pLivingEntity, new Vec3(x, y, z));
    }
}

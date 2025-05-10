package net.eclipixie.chorusmod.item.custom;

import net.eclipixie.chorusmod.item.ModArmorMaterials;
import net.eclipixie.chorusmod.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SculkSonicChannelerItem extends Item {
    private static final double KB_SCALAR = 0.05;

    public SculkSonicChannelerItem(Properties pProperties) {
        super(pProperties);
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        pLevel.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_CHARGE,
                SoundSource.PLAYERS, 1f, 1f);

        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }

    private static boolean checkSonic(Player pPlayer) {
        InteractionHand hand = pPlayer.getUsedItemHand();
        ItemStack stack = pPlayer.getItemInHand(hand);
        boolean isChanneler = stack.is(ModItems.SCULK_SONIC_CHANNELER.get());

        return ArmorSetCore.checkSet(ModArmorMaterials.SCULK_EXOSKELETON, pPlayer) && // check set
                isChanneler && // correct item
                !pPlayer.getCooldowns().isOnCooldown(ModItems.SCULK_SONIC_CHANNELER.get()) && // off cooldown
                pPlayer.isUsingItem(); // currently using
    }

    private static void sonicGlobals(Player pPlayer) {
        pPlayer.getCooldowns().addCooldown(ModItems.SCULK_SONIC_CHANNELER.get(),
                40);
        pPlayer.stopUsingItem();
    }

    public static boolean checkSonicPassthrough(Level pLevel, Vec3 pFrom, Vec3 pTo) {
        return pLevel.isBlockInLine(new ClipBlockStateContext(pFrom, pTo,
                (state) -> state.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS)))
                .getType() != HitResult.Type.BLOCK;
    }

    public static void sonicDash(Player pPlayer) {
        Level level = pPlayer.level();

        if (checkSonic(pPlayer)) {
            sonicGlobals(pPlayer);

            Vec3 dir = pPlayer.getDeltaMovement();
            dir = new Vec3(dir.x, 0, dir.z)
                    .normalize();
            dir = dir.add(new Vec3(0, .15, 0))
                    .scale(1.5);

            if (level.isClientSide()) {
                level.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                        SoundSource.PLAYERS, 1f, 1f);

                pPlayer.push(dir.x, dir.y, dir.z);
            }
            else {
                ServerLevel serverLevel = (ServerLevel) level;

                serverLevel.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                        SoundSource.PLAYERS, 1f, 1f);

                Vec3 pos = pPlayer.getEyePosition().add(0, -.5, 0);

                for (int i = 0; i < 3; i++) {
                    serverLevel.sendParticles(ParticleTypes.SONIC_BOOM,
                            pos.x() + dir.x() * i, pos.y() + dir.y() * i, pos.z() + dir.z() * i, 1,
                            0, 0, 0, 0);
                }
            }
        }
    }

    public static void sonicRoar(Player pPlayer) {
        Level level = pPlayer.level();

        if (checkSonic(pPlayer)) {
            sonicGlobals(pPlayer);

            if (level.isClientSide()) {
                level.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                        SoundSource.PLAYERS, 1f, 1f);
            }
            else {
                ServerLevel serverLevel = (ServerLevel) level;

                serverLevel.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                        SoundSource.PLAYERS, 1f, 1f);

                double range = 5;

                Vec3 pos = pPlayer.getEyePosition().add(0, 0, 0);

                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM,
                        pos.x(), pos.y(), pos.z(), 24,
                        range * .5, range * .5, range * .5, 0);

                //#region sonic roar
                for(LivingEntity livingentity :
                        serverLevel.getEntitiesOfClass(LivingEntity.class, pPlayer.getBoundingBox().inflate(range))) {
                    // disable friendly fire
                    if (livingentity.getUUID() == pPlayer.getUUID()) continue;
                    // check if the entity is within range
                    if (!(pPlayer.distanceToSqr(livingentity) > Mth.square(range))) {
                        if (checkSonicPassthrough(serverLevel, pPlayer.getEyePosition(), livingentity.position())) {
                            float finalDamage = (float) (12.) *
                                    (float)Math.sqrt((range - pPlayer.getEyePosition().distanceTo(livingentity.position())) / range) +
                                    (float) (2.);

                            livingentity.hurt(pPlayer.damageSources().magic(), finalDamage);

                            Vec3 impulse = livingentity.position().subtract(pPlayer.getEyePosition())
                                    .normalize()
                                    .scale(finalDamage * KB_SCALAR)
                                    .scale(1. - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));

                            livingentity.push(impulse.x(), impulse.y(), impulse.z());
                        }
                    }
                }
                //#endregion
            }
        }
    }

    public static void sonicBlast(Player pPlayer) {
        Level level = pPlayer.level();

        if (checkSonic(pPlayer)) {
            sonicGlobals(pPlayer);

            if (level.isClientSide()) {
                level.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                        SoundSource.PLAYERS, 1f, 1f);

                Vec3 impulse = pPlayer.getLookAngle().normalize().scale(-.6);

                pPlayer.push(impulse.x(), impulse.y(), impulse.z());
            }
            else {
                ServerLevel serverLevel = (ServerLevel) level;

                serverLevel.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                        SoundSource.PLAYERS, 1f, 1f);

                Vec3 dir = pPlayer.getLookAngle().normalize();
                Vec3 point = pPlayer.getEyePosition();

                for (int i = 0; i < 7 * 2; i++) {
                    point = point.add(dir.scale(0.5));

                    if (i % 2 == 0) {
                        serverLevel.sendParticles(ParticleTypes.SONIC_BOOM,
                                point.x(), point.y(), point.z(), 1,
                                0, 0, 0, 0);
                    }

                    for(LivingEntity livingentity :
                            serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(point, point)
                                    .inflate(1))) {
                        if (livingentity.getUUID() == pPlayer.getUUID()) continue;
                        if (!checkSonicPassthrough(serverLevel, pPlayer.getEyePosition(), livingentity.getEyePosition())) continue;

                        float damage = 10;

                        livingentity.hurt(pPlayer.damageSources().magic(), damage);

                        Vec3 impulse = dir.scale(damage * 0.6 * KB_SCALAR)
                                .scale(1. - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));

                        livingentity.push(impulse.x(), impulse.y(), impulse.z());
                    }
                }
            }
        }
    }
}

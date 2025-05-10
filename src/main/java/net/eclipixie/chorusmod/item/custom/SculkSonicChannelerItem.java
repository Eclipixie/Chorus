package net.eclipixie.chorusmod.item.custom;

import net.eclipixie.chorusmod.item.ModArmorMaterials;
import net.eclipixie.chorusmod.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SculkSonicChannelerItem extends Item {
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
    }

    public static void sonicJump(Player pPlayer) {
        Level level = pPlayer.level();

        if (level.isClientSide && checkSonic(pPlayer)) {
            level.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                    SoundSource.PLAYERS, 1f, 1f);
            sonicGlobals(pPlayer);

            pPlayer.push(0, 1.5, 0);
        }
    }

    public static void sonicDash(Player pPlayer) {
        Level level = pPlayer.level();

        if (level.isClientSide && checkSonic(pPlayer)) {
            level.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                    SoundSource.PLAYERS, 1f, 1f);
            sonicGlobals(pPlayer);

            Vec3 pos = pPlayer.getEyePosition();

            level.addParticle(ParticleTypes.SONIC_BOOM,
                    pos.x(), pos.y() - 1, pos.z(),
                    0, 0, 0);

            Vec3 dir = pPlayer.getDeltaMovement();
            dir = new Vec3(dir.x, 0, dir.z)
                    .normalize();
            dir = dir.add(new Vec3(0, .15, 0))
                    .scale(3);

            pPlayer.push(dir.x, dir.y, dir.z);
        }
    }

    public static void sonicRoar(Player pPlayer) {
        Level level = pPlayer.level();
        
        if (checkSonic(pPlayer)) {
            if (level.isClientSide) {
                level.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.WARDEN_SONIC_BOOM,
                        SoundSource.PLAYERS, 1f, 1f);
                return;
            }
            
            sonicGlobals(pPlayer);

            Vec3 vec3 = pPlayer.position();

            double range = 8;

            for(LivingEntity livingentity : level.getEntitiesOfClass(LivingEntity.class, pPlayer.getBoundingBox().inflate(5.0D))) {
                // disable friendly fire
                if (livingentity.getUUID() == pPlayer.getUUID()) continue;
                // check if the entity is within 5 blocks
                if (!(pPlayer.distanceToSqr(livingentity) > Mth.square(range))) {
                    boolean doDamage = false;

                    // checking both y values for 2-high entities i think?
                    for(int i = 0; i < 2; ++i) {
                        // position
                        Vec3 vec31 = new Vec3(livingentity.getX(), livingentity.getY(0.5D * (double)i), livingentity.getZ());

                        // check blocks in line for vibration occlusions
                        if (level.isBlockInLine(new ClipBlockStateContext(vec3, vec31,
                                (state) -> state.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))) // only protect with vibration occluders
                                .getType() != HitResult.Type.BLOCK) {
                            doDamage = true;
                            break;
                        }
                    }

                    if (doDamage) {
                        float finalDamage = (float) (7.) *
                                (float)Math.sqrt((range - (double)pPlayer.distanceTo(livingentity)) / range) +
                                (float) (2.);

                        livingentity.hurt(pPlayer.damageSources().magic(), finalDamage);
                    }
                }
            }
        }
    }
}

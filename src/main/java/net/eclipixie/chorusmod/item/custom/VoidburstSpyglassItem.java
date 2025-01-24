package net.eclipixie.chorusmod.item.custom;

import net.eclipixie.chorusmod.mobeffects.ModMobEffects;
import net.eclipixie.chorusmod.util.Teleports;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class VoidburstSpyglassItem extends SpyglassItem {
    public VoidburstSpyglassItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pPlayer.getCooldowns().isOnCooldown(this)) {
            pPlayer.getCooldowns().addCooldown(this, 20);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        boolean cd = false;

        if (pLivingEntity instanceof Player) {
            cd = ((Player)pLivingEntity).getCooldowns().isOnCooldown(this);
        }

        if (pLivingEntity.isCrouching() && !cd) {
            // set cooldowns immediately
            if (pLivingEntity instanceof Player) {
                System.out.println("used");
                ((Player)pLivingEntity).getCooldowns().addCooldown(this, 200);
            }

            // status effects
            pLivingEntity.addEffect(new MobEffectInstance(
                    ModMobEffects.VOID_INSTABILITY.get(),
                    Teleports.voidburstInstabilityDuration,
                    0)
            );

            // raycast
            Vec3 origin = pLivingEntity.getEyePosition();
            Vec3 target = origin.add(pLivingEntity.getLookAngle().normalize().scale(50.));
            BlockHitResult hitResult = pLevel.clip(new ClipContext(
                    pLivingEntity.getEyePosition(),
                    target,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.ANY,
                    null
                )
            );

            // teleport
            Teleports.VoidburstTeleport(pLevel, pLivingEntity,
                    hitResult.getBlockPos().getX() + 0.5,
                    hitResult.getBlockPos().getY() + 1,
                    hitResult.getBlockPos().getZ() + 0.5
            );
        }
    }
}

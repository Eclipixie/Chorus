package net.eclipixie.chorusmod.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DigeridooItem extends Item {
    public DigeridooItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pLevel.playSound(pPlayer, pPlayer.getOnPos(), SoundEvents.NOTE_BLOCK_DIDGERIDOO.get(),
                SoundSource.BLOCKS, 1f, 1f);

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}

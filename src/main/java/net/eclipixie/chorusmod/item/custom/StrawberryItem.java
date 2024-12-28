package net.eclipixie.chorusmod.item.custom;

import net.eclipixie.chorusmod.util.Teleports;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StrawberryItem extends Item {
    public StrawberryItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        Teleports.RandomTeleport(pLevel, pLivingEntity);
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}

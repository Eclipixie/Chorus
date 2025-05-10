package net.eclipixie.chorusmod.item.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class ArmorSetCore extends ArmorItem {
    public ArmorSetCore(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    public static boolean checkSet(ArmorMaterial material, Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.isEmpty()) return false; // empty stack
            if (!(stack.getItem() instanceof ArmorItem)) return false; // elytra etc
            if (((ArmorItem) stack.getItem()).getMaterial() != material) return false; // checking for same material
        }

        return true;
    }
}

package net.eclipixie.chorusmod.item.custom.armor;

import net.eclipixie.chorusmod.entity.client.model.SculkExoskeletonModel3;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class SculkExoskeletonCore extends ArmorItem {
    private static Object armorModel = null;

    public SculkExoskeletonCore(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
}

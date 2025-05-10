package net.eclipixie.chorusmod.item.custom.armor;

import net.eclipixie.chorusmod.entity.client.renderer.SculkExoskeletonRenderer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class SculkExoskeletonArmor extends ArmorItem {
    private static Object armorModel = null;

    public static SculkExoskeletonRenderer renderer = null;

    public SculkExoskeletonArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {

    }
}

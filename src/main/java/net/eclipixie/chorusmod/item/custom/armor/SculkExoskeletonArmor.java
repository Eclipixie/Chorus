package net.eclipixie.chorusmod.item.custom.armor;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.entity.client.model.SculkExoskeletonModel3;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SculkExoskeletonArmor extends ArmorItem {
    public SculkExoskeletonArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public boolean canBeDepleted() { return false; }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return new SculkExoskeletonModel3(SculkExoskeletonModel3.createBodyLayer().bakeRoot());
            }
        });
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        String path = ChorusMod.MOD_ID + ":textures/entity/armor/sculk_exoskeleton_3";
        path += Objects.equals(type, "emissive") ? "_emissive" : "";
        path += "_extension_2d.png";

        return path;
    }

    public String getSkinTexture(String type) {
        String path = ChorusMod.MOD_ID + ":textures/entity/armor/sculk_exoskeleton_3";
        path += Objects.equals(type, "emissive") ? "_emissive" : "";
        path += "_skin_2d.png";

        return path;
    }

    public float getGlowValue(int pPackedLight) {
        int lig = Math.max(LightTexture.sky(pPackedLight), LightTexture.block(pPackedLight));

        return 1F - (lig / 15F);
    }
}

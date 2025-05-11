package net.eclipixie.chorusmod.item.custom.armor;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.entity.client.model.SculkExoskeletonModel3;
import net.eclipixie.chorusmod.item.ModArmorMaterials;
import net.eclipixie.chorusmod.item.custom.ArmorSetCore;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SculkExoskeletonArmor extends ArmorItem {
    public SculkExoskeletonArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player && this.type == Type.CHESTPLATE) {
            player.setInvisible(ArmorSetCore.checkSet(ModArmorMaterials.SCULK_EXOSKELETON, player));
        }
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
        return Objects.equals(type, "emissive") ? ChorusMod.MOD_ID + ":textures/entity/armor/sculk_exoskeleton_3_emissive_2d.png" :
                ChorusMod.MOD_ID + ":textures/entity/armor/sculk_exoskeleton_3_2d.png";
    }

    public float getGlowValue(int pPackedLight) {
        int lig = Math.max(LightTexture.sky(pPackedLight), LightTexture.block(pPackedLight));

        return 1F - (lig / 15F);
    }
}

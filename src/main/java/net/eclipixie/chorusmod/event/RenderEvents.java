package net.eclipixie.chorusmod.event;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.client.render.layer.SkinOverrideLayer;
import net.eclipixie.chorusmod.item.ModItems;
import net.eclipixie.chorusmod.item.custom.armor.SculkExoskeletonArmor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEvents {

}

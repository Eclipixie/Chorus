package net.eclipixie.chorusmod.event;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.block.client.SculkCorruptedEndermanBlockRenderer;
import net.eclipixie.chorusmod.block.client.SculkHarvesterBlockRenderer;
import net.eclipixie.chorusmod.block.entity.ModBlockEntities;
import net.eclipixie.chorusmod.client.render.layer.SkinOverrideLayer;
import net.eclipixie.chorusmod.entity.ModEntities;
import net.eclipixie.chorusmod.entity.client.model.SculkExoskeletonModel3;
import net.eclipixie.chorusmod.entity.client.renderer.PearlBombEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(
                ModEntities.PEARL_BOMB.get(),
                PearlBombEntityRenderer::new);

        BlockEntityRenderers.register(
                ModBlockEntities.SCULK_CORRUPTED_ENDERMAN_ENTITY.get(),
                SculkCorruptedEndermanBlockRenderer::new);
        BlockEntityRenderers.register(
                ModBlockEntities.SCULK_HARVESTER_ENTITY.get(),
                SculkHarvesterBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                SculkExoskeletonModel3.LAYER_LOCATION,
                SculkExoskeletonModel3::createBodyLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            LivingEntityRenderer playerSkin = event.getSkin(skin);

            if (playerSkin instanceof PlayerRenderer playerRenderer) {
                playerSkin.addLayer(new SkinOverrideLayer<>(playerRenderer));
            }
        }
    }
}

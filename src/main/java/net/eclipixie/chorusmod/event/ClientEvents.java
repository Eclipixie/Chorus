package net.eclipixie.chorusmod.event;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.entity.client.model.SculkExoskeletonLayer;
import net.eclipixie.chorusmod.entity.client.model.SculkExoskeletonModel3;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                SculkExoskeletonModel3.LAYER_LOCATION,
                SculkExoskeletonModel3::createBodyLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            addPlayerLayer(event, skin);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addPlayerLayer(EntityRenderersEvent.AddLayers event, String skin) {
        EntityRenderer<? extends Player> renderer = event.getSkin(skin);

        if (renderer instanceof LivingEntityRenderer livingEntityRenderer) {
            livingEntityRenderer.addLayer(new SculkExoskeletonLayer(livingEntityRenderer));
        }
    }
}

package net.eclipixie.chorusmod;

import com.mojang.logging.LogUtils;
import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.block.client.SculkCorruptedEndermanBlockRenderer;
import net.eclipixie.chorusmod.block.client.SculkHarvesterBlockRenderer;
import net.eclipixie.chorusmod.block.entity.ModBlockEntities;
import net.eclipixie.chorusmod.entity.ModEntities;
import net.eclipixie.chorusmod.entity.client.PearlBombEntityRenderer;
import net.eclipixie.chorusmod.event.BlockEvents;
import net.eclipixie.chorusmod.event.EntityEvents;
import net.eclipixie.chorusmod.item.ModCreativeModeTabs;
import net.eclipixie.chorusmod.item.ModItems;
import net.eclipixie.chorusmod.mobeffects.ModMobEffects;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ChorusMod.MOD_ID)
public class ChorusMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "chorusmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ChorusMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModMobEffects.register(modEventBus);

        BlockEvents.register(MinecraftForge.EVENT_BUS);
        EntityEvents.register(MinecraftForge.EVENT_BUS);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
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
    }
}

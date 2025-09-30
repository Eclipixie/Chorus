package net.eclipixie.chorusmod;

import com.mojang.logging.LogUtils;
import net.eclipixie.chorusmod.block.ModBlocks;
import net.eclipixie.chorusmod.block.entity.ModBlockEntities;
import net.eclipixie.chorusmod.entity.ModEntities;
import net.eclipixie.chorusmod.event.BlockEvents;
import net.eclipixie.chorusmod.event.EntityEvents;
import net.eclipixie.chorusmod.fluid.ModFluids;
import net.eclipixie.chorusmod.item.ModCreativeModeTabs;
import net.eclipixie.chorusmod.item.ModItems;
import net.eclipixie.chorusmod.mobeffects.ModMobEffects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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

    public ChorusMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModFluids.register(modEventBus);
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
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}

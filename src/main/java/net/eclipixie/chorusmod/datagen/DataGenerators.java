package net.eclipixie.chorusmod.datagen;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.compat.fusion.ModelProvider;
import net.eclipixie.chorusmod.compat.fusion.TextureMetadataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // server generators
        generator.addProvider(event.includeServer(), new ModRecipeProvider(
                packOutput));
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(
                packOutput));
        ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(), new ModBlockTagGenerator(
                packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModItemTagGenerator(
                packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

        // client generators
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(
                packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(
                packOutput, existingFileHelper));
        // fusion
        generator.addProvider(event.includeClient(), new TextureMetadataProvider(
                packOutput));
        generator.addProvider(event.includeClient(), new ModelProvider(
                packOutput, existingFileHelper));
    }
}

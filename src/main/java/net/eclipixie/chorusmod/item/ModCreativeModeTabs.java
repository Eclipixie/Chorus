package net.eclipixie.chorusmod.item;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChorusMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CHORUS_TAB = CREATIVE_MODE_TABS.register("chorus_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ENDER_SHARD.get()))
                    .title(Component.translatable("creativetab.chorus_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.VOIDBURST_SPYGLASS.get());
                        output.accept(ModItems.SOUNDSIGHT_SPYGLASS.get());

                        output.accept(ModItems.ENDER_SHARD.get());

                        output.accept(ModBlocks.VOIDGLASS.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

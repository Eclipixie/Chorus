package net.eclipixie.chorusmod.item;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.item.custom.*;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ChorusMod.MOD_ID);

    public static final RegistryObject<Item> VOIDBURST_SPYGLASS = ITEMS.register("voidburst_spyglass",
            () -> new VoidburstSpyglassItem(new Item.Properties()));
    public static final RegistryObject<Item> SOUNDSIGHT_SPYGLASS = ITEMS.register("soundsight_spyglass",
            () -> new SoundsightSpyglassItem(new Item.Properties()));

    public static final RegistryObject<Item> ENDER_SHARD = ITEMS.register("ender_shard",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

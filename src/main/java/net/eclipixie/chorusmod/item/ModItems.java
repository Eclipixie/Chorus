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

    public static final RegistryObject<Item> SCULK_EXOSKELETON_HELMET = ITEMS.register("sculk_exoskeleton_helmet",
            () -> new ArmorItem(ModArmorMaterials.SCULK_EXOSKELETON, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SCULK_EXOSKELETON_CHESTPLATE = ITEMS.register("sculk_exoskeleton_chestplate",
            () -> new ArmorItem(ModArmorMaterials.SCULK_EXOSKELETON, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SCULK_EXOSKELETON_LEGGINGS = ITEMS.register("sculk_exoskeleton_leggings",
            () -> new ArmorItem(ModArmorMaterials.SCULK_EXOSKELETON, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SCULK_EXOSKELETON_BOOTS = ITEMS.register("sculk_exoskeleton_boots",
            () -> new ArmorItem(ModArmorMaterials.SCULK_EXOSKELETON, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> SCULK_SONIC_CHANNELER = ITEMS.register("sculk_sonic_channeler",
            () -> new SculkSonicChannelerItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

package net.eclipixie.chorusmod.fluid;

import net.eclipixie.chorusmod.ChorusMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ChorusMod.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, ChorusMod.MOD_ID);

    public static final FluidRegistryContainer LIQUID_XP = new FluidRegistryContainer("liquid_xp",
            FluidType.Properties.create()
                    .density(15)
                    .viscosity(5)
                    .canHydrate(false)
                    .canConvertToSource(false)
                    .canDrown(true)
                    .lightLevel(13),
            () -> FluidRegistryContainer.createExtension(
                    new FluidRegistryContainer.ClientExtensions(ChorusMod.MOD_ID, "liquid_xp")
                            .tint(0xFF44AA)
                            .fogColor(1.0f, 0.2f, 0.5f)),
            BlockBehaviour.Properties.copy(Blocks.WATER),
            new Item.Properties().stacksTo(1));

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }
}

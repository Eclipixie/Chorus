package net.eclipixie.chorusmod.mobeffects;

import net.eclipixie.chorusmod.ChorusMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ChorusMod.MOD_ID);

    public static final RegistryObject<MobEffect> VOID_INSTABILITY = EFFECTS.register("void_instability",
            () -> new ModMobEffect(MobEffectCategory.HARMFUL, 16347372));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}

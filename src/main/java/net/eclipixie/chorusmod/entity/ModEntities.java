package net.eclipixie.chorusmod.entity;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.entity.custom.PearlBombEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ChorusMod.MOD_ID);

    public static final RegistryObject<EntityType<PearlBombEntity>> PEARL_BOMB = (RegistryObject<EntityType<PearlBombEntity>>) ENTITIES.register("pearl_bomb",
            () -> EntityType.Builder.of(PearlBombEntity::new, MobCategory.MISC)
                    .sized(.4f, .4f)
                    .build("pearl_bomb"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}

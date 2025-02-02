package net.eclipixie.chorusmod.block.entity;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChorusMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<SculkCorruptedEndermanBlockEntity>> SCULK_CORRUPTED_ENDERMAN_ENTITY =
            BLOCK_ENTITIES.register("sculk_corrupted_enderman",
                    () -> BlockEntityType.Builder.of(
                            SculkCorruptedEndermanBlockEntity::new,
                            ModBlocks.SCULK_CORRUPTED_ENDERMAN.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<SculkHarvesterBlockEntity>> SCULK_HARVESTER_ENTITY =
            BLOCK_ENTITIES.register("sculk_harvester",
                    () -> BlockEntityType.Builder.of(
                            SculkHarvesterBlockEntity::new,
                            ModBlocks.SCULK_HARVESTER.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

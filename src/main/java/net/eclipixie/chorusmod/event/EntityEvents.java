package net.eclipixie.chorusmod.event;

import net.eclipixie.chorusmod.entity.ModEntities;
import net.eclipixie.chorusmod.entity.custom.PearlBombEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;

public class EntityEvents {
    @SubscribeEvent
    public static void onLivingDrop(LivingDropsEvent event) {
        Level level = event.getEntity().getCommandSenderWorld();
        if (level.isClientSide()) return;
        Collection<ItemEntity> entities = event.getDrops();
        String dimension = level.dimension().location().getPath();

        if (!dimension.equals("the_end")) return;
        for (ItemEntity ent : entities) {
            if (ent.getItem().is(Items.ENDER_PEARL)) {
                // summon bomb
                PearlBombEntity newEntity = new PearlBombEntity(ModEntities.PEARL_BOMB.get(), level);
                newEntity.setPos(event.getEntity().position().add(0, 1, 0));
                System.out.println(level.addFreshEntity(newEntity));

                // die
                ent.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(EntityEvents::onLivingDrop);
    }
}

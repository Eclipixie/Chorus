package net.eclipixie.chorusmod.event;

import net.minecraft.client.gui.font.providers.UnihexProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;

public class EntityEvents {
    @SubscribeEvent
    public static void onLivingDrop(LivingDropsEvent event) {
        Collection<ItemEntity> entities = event.getDrops();
        String dimension = event.getEntity().getCommandSenderWorld().dimension().location().getPath();

        if (!dimension.equals("the_end")) return;
        for (ItemEntity ent : entities) {
            if (ent.getItem().is(Items.ENDER_PEARL)) {
                System.out.println("yes rico, kaboom");
            }
        }
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(EntityEvents::onLivingDrop);
    }
}

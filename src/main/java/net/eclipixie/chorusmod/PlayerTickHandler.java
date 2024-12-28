package net.eclipixie.chorusmod;

import net.eclipixie.chorusmod.item.custom.VoidburstSpyglassItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerTickHandler {
//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        if (event.phase == TickEvent.Phase.START && event.player.isUsingItem() && event.player.getUseItem().getItem() instanceof VoidburstSpyglassItem) {
//            float targetFov = 30.0f;  // Adjust this value as necessary for your zoom effect
//            Minecraft.getInstance().gameSettings.fov = MathHelper.lerp(0.1f, Minecraft.getInstance().options.fov(), targetFov);
//        }
//    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player.isUsingItem() && event.player.getUseItem().getItem() instanceof VoidburstSpyglassItem) {
            // Modify FOV using the player's options
            Minecraft.getInstance().options.fov().set(30); // Example: set to a low value for zoom effect
        } else {
            // Reset FOV if the spyglass is not in use
            Minecraft.getInstance().options.fov().set(70); // Reset to default FOV
        }
    }
}

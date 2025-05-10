package net.eclipixie.chorusmod;

import net.eclipixie.chorusmod.item.custom.SculkSonicChannelerItem;
import net.eclipixie.chorusmod.item.custom.VoidburstSpyglassItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerTickHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player.isUsingItem() && event.player.getUseItem().getItem() instanceof VoidburstSpyglassItem) {
            // Modify FOV using the player's options
            Minecraft.getInstance().options.fov().set(30); // Example: set to a low value for zoom effect
        } else {
            // Reset FOV if the spyglass is not in use
            Minecraft.getInstance().options.fov().set(70); // Reset to default FOV
        }

        if (event.phase == TickEvent.Phase.END) {
            if (event.player.level().isClientSide) {
                if (Minecraft.getInstance().options.keySprint.isDown()) {
                    SculkSonicChannelerItem.sonicDash(event.player);
                }
                if (Minecraft.getInstance().options.keyJump.isDown()) {
                    SculkSonicChannelerItem.sonicJump(event.player);
                }
            }

            if (event.player.isShiftKeyDown()) {
                SculkSonicChannelerItem.sonicRoar(event.player);
            }
        }
    }
}

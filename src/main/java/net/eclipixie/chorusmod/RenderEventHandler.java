package net.eclipixie.chorusmod;

import net.eclipixie.chorusmod.item.custom.SoundsightSpyglassItem;
import net.eclipixie.chorusmod.item.custom.VoidburstSpyglassItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Type;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEventHandler {
    public static final ResourceLocation VOIDBURST_SPYGLASS_SCOPE = new ResourceLocation(
            "chorusmod", "textures/gui/voidburst_spyglass_zoom_overlay.png");
    public static final ResourceLocation SOUNDSIGHT_SPYGLASS_SCOPE = new ResourceLocation(
            "chorusmod", "textures/gui/soundsight_spyglass_zoom_overlay.png");

    public static final Map<Type, ResourceLocation> SCOPE_MAP = Map.of(
            VoidburstSpyglassItem.class, VOIDBURST_SPYGLASS_SCOPE,
            SoundsightSpyglassItem.class, SOUNDSIGHT_SPYGLASS_SCOPE
    );

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent event) {
//        if (Minecraft.getInstance().player != null &&
//                Minecraft.getInstance().player.isUsingItem() &&
//                Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
//            Type item = Minecraft.getInstance().player.getUseItem().getItem().getClass();
//            if (SCOPE_MAP.containsKey( item )) {
//                // Use GuiGraphics to render
//                int wWidth = Minecraft.getInstance().getWindow().getWidth() / 2;
//                int wHeight = Minecraft.getInstance().getWindow().getHeight() / 2;
//                int texWidth = 64;
//                int texHeight = 32;
//                double texAsp = ((double) texHeight) / ((double) texWidth);
//                double asp = ((double) wHeight) / ((double) wWidth);
//                int offsetX = (int) (wWidth * (texAsp - asp));
//
//                RenderSystem.enableBlend();
//                RenderSystem.blendFuncSeparate(
//                        GlStateManager.SourceFactor.SRC_ALPHA,
//                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
//                        GlStateManager.SourceFactor.ONE,
//                        GlStateManager.DestFactor.ZERO
//                );
//                event.getGuiGraphics().blit(SCOPE_MAP.get(item),
//                offsetX, 0,
//                        ((int) Math.round(((double) wWidth) * asp / texAsp)), wHeight,
//                        0, 0,
//                        texWidth, texHeight,
//                        texWidth, texHeight
//                );
//                RenderSystem.defaultBlendFunc();
//                RenderSystem.disableBlend();
//            }
//        }
    }
}

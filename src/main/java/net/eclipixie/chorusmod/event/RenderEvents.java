package net.eclipixie.chorusmod.event;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.item.custom.SoundsightSpyglassItem;
import net.eclipixie.chorusmod.item.custom.VoidburstSpyglassItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Type;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ChorusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEvents {
    public static final ResourceLocation VOIDBURST_SPYGLASS_SCOPE = new ResourceLocation(
            "chorusmod", "textures/gui/voidburst_spyglass_zoom_overlay.png");
    public static final ResourceLocation SOUNDSIGHT_SPYGLASS_SCOPE = new ResourceLocation(
            "chorusmod", "textures/gui/soundsight_spyglass_zoom_overlay.png");

    public static final Map<Type, ResourceLocation> SCOPE_MAP = Map.of(
            VoidburstSpyglassItem.class, VOIDBURST_SPYGLASS_SCOPE,
            SoundsightSpyglassItem.class, SOUNDSIGHT_SPYGLASS_SCOPE
    );
}

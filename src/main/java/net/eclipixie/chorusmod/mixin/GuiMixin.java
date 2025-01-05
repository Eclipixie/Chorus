package net.eclipixie.chorusmod.mixin;

import net.eclipixie.chorusmod.item.custom.SoundsightSpyglassItem;
import net.eclipixie.chorusmod.item.custom.VoidburstSpyglassItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpyglassItem;
import org.spongepowered.asm.mixin.*;

import java.lang.reflect.Type;
import java.util.Map;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    protected int screenWidth;
    @Shadow
    protected int screenHeight;
    @Final @Shadow
    protected static ResourceLocation SPYGLASS_SCOPE_LOCATION;

    @Unique
    private static final Map<Type, ResourceLocation> SPYGLASS_SCOPE_LOCATIONS = Map.of(
            VoidburstSpyglassItem.class, new ResourceLocation(
                    "chorusmod", "textures/gui/voidburst_spyglass_zoom_overlay.png"),
            SoundsightSpyglassItem.class, new ResourceLocation(
                    "chorusmod", "textures/gui/soundsight_spyglass_zoom_overlay.png"),
            SpyglassItem.class, SPYGLASS_SCOPE_LOCATION
    );

    /**
     * @author Eclipixie
     * @reason Custom spyglass HUD overlays
     */
    @Overwrite
    public void renderSpyglassOverlay(GuiGraphics pGuiGraphics, float pScopeScale) {
        Item heldItem = Minecraft.getInstance().player.getUseItem().getItem();
        Type heldItemType = heldItem.getClass();

        if (!SPYGLASS_SCOPE_LOCATIONS.containsKey(heldItemType)) {
            return;
        }

        float f = (float)Math.min(this.screenWidth, this.screenHeight);
        float f1 = Math.min((float)this.screenWidth / f, (float)this.screenHeight / f) * pScopeScale;
        int i = Mth.floor(f * f1);
        int j = Mth.floor(f * f1);
        int k = (this.screenWidth - i) / 2;
        int l = (this.screenHeight - j) / 2;
        int i1 = k + i;
        int j1 = l + j;
        pGuiGraphics.blit(SPYGLASS_SCOPE_LOCATIONS.get(heldItemType), k, l, -90, 0.0F, 0.0F, i, j, i, j);
        pGuiGraphics.fill(RenderType.guiOverlay(), 0, j1, this.screenWidth, this.screenHeight, -90, -16777216);
        pGuiGraphics.fill(RenderType.guiOverlay(), 0, 0, this.screenWidth, l, -90, -16777216);
        pGuiGraphics.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, -16777216);
        pGuiGraphics.fill(RenderType.guiOverlay(), i1, l, this.screenWidth, j1, -90, -16777216);
    }
}

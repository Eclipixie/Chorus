package net.eclipixie.chorusmod.itemRenderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.item.ItemStack;

public class VoidburstSpyglassRenderer extends ItemRenderer {
    public VoidburstSpyglassRenderer(Minecraft pMinecraft, TextureManager pTextureManager, ModelManager pModelManager, ItemColors pItemColors, BlockEntityWithoutLevelRenderer pBlockEntityRenderer) {
        super(pMinecraft, pTextureManager, pModelManager, pItemColors, pBlockEntityRenderer);
    }

//    @Override
//    public void render(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
//        if (transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
//            // Custom rendering logic for the spyglass when held in the player's hand
//            // For example, use model loading and rendering code here
//            // Custom model, texture, or effect to show the spyglass in the player's hand
//            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, transformType, poseStack, buffer, light, overlay);
//            Minecraft.getInstance().getItemRenderer().renderStatic();
//        }
//    }
}

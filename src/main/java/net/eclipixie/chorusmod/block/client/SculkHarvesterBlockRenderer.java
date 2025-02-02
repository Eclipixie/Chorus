package net.eclipixie.chorusmod.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.eclipixie.chorusmod.block.entity.SculkHarvesterBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SculkHarvesterBlockRenderer implements BlockEntityRenderer<SculkHarvesterBlockEntity>  {
    public SculkHarvesterBlockRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(SculkHarvesterBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack inputStack = pBlockEntity.getItem(SculkHarvesterBlockEntity.INPUT_SLOT).copy();
        ItemStack processingStack = inputStack.copy();
        inputStack.setCount(inputStack.getCount() - 1);
        ItemStack outputStack = pBlockEntity.getItem(SculkHarvesterBlockEntity.OUTPUT_SLOT).copy();

        pPoseStack.pushPose();

        // 8 / 16, 12 / 16, 2 / 16
        pPoseStack.translate(.5, .75f, .125f);

        pPoseStack.scale(.5f, .5f, .5f);

        itemRenderer.renderStatic(
                inputStack,
                ItemDisplayContext.FIXED,
                pPackedLight,
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pBlockEntity.getLevel(), 1);

        pPoseStack.scale(2f, 2f, 2f);

        // 0, 0, 12 / 16
        pPoseStack.translate(0, 0, 0.75f);

        pPoseStack.scale(.5f, .5f, .5f);

        itemRenderer.renderStatic(
                outputStack,
                ItemDisplayContext.FIXED,
                pPackedLight,
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pBlockEntity.getLevel(), 1);

        pPoseStack.scale(2f, 2f, 2f);

        // 0, -2 / 16, -6 / 16
        pPoseStack.translate(0, -0.125, -0.375);
        pPoseStack.mulPose(Axis.ZN.rotationDegrees(180));

        pPoseStack.scale(.5f, .5f, .5f);

        itemRenderer.renderStatic(
                processingStack,
                ItemDisplayContext.FIXED,
                pPackedLight,
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pBlockEntity.getLevel(), 1);

        pPoseStack.popPose();
    }
}

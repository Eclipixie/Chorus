package net.eclipixie.chorusmod.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.eclipixie.chorusmod.block.custom.SculkCorruptedEndermanBlock;
import net.eclipixie.chorusmod.block.entity.SculkCorruptedEndermanBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SculkCorruptedEndermanBlockRenderer implements BlockEntityRenderer<SculkCorruptedEndermanBlockEntity> {
    public SculkCorruptedEndermanBlockRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(SculkCorruptedEndermanBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.getBlockState().getValue(SculkCorruptedEndermanBlock.IS_TOP)) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = new ItemStack(Items.ECHO_SHARD);

        pPoseStack.pushPose();

        int rot = 0;

        switch (pBlockEntity.getBlockState().getValue(SculkCorruptedEndermanBlock.FACING)) {
            case NORTH -> rot = 270;
            case EAST ->  rot = 0;
            case SOUTH -> rot = 90;
            case WEST ->  rot = 180;
        }

        pPoseStack.translate(0.5, 2.1, 0.5);
        pPoseStack.mulPose(Axis.YN.rotationDegrees(rot));

        itemRenderer.renderStatic(
                itemStack,
                ItemDisplayContext.FIXED,
                pPackedLight,
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pBlockEntity.getLevel(), 1);

        pPoseStack.popPose();
    }
}

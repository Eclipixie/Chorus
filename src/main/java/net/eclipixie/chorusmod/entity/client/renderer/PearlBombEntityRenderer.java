package net.eclipixie.chorusmod.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.eclipixie.chorusmod.entity.custom.PearlBombEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PearlBombEntityRenderer extends EntityRenderer<PearlBombEntity> {
    public PearlBombEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(PearlBombEntity pEntity) {
        return null;
    }

    @Override
    public void render(PearlBombEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = new ItemStack(Items.ENDER_PEARL, 1);

        pPoseStack.pushPose();

        pPoseStack.scale(.8f, .8f, .8f);
        pPoseStack.mulPose(Axis.YN.rotationDegrees(
                (float) Math.pow(pEntity.tickCount * PearlBombEntity.SPIN_SPEED, PearlBombEntity.SPIN_ACCEL)));

        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                getBlockLightLevel(pEntity, pEntity.blockPosition()),
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pEntity.level(), 1);

        pPoseStack.popPose();
    }
}

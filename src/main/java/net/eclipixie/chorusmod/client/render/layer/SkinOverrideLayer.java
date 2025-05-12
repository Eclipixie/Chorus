package net.eclipixie.chorusmod.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.eclipixie.chorusmod.item.custom.armor.SculkExoskeletonArmor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class SkinOverrideLayer<E extends Entity, M extends EntityModel<E>> extends RenderLayer<E, M> {

    public SkinOverrideLayer(RenderLayerParent<E, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, E pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        for (ItemStack iStack : pLivingEntity.getArmorSlots()) {
            if (iStack.getItem() instanceof SculkExoskeletonArmor sculkExoskeletonArmor) {
                VertexConsumer vertexConsumer = pBuffer.getBuffer(
                    RenderType.entityCutoutNoCull(
                            new ResourceLocation(sculkExoskeletonArmor.getSkinTexture(null))));

                getParentModel().renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY,
                        1F, 1F, 1F, 1F);

                vertexConsumer = pBuffer.getBuffer(
                        RenderType.entityTranslucentEmissive(
                                new ResourceLocation(sculkExoskeletonArmor.getSkinTexture("emissive"))));

                getParentModel().renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY,
                        1F, 1F, 1F, sculkExoskeletonArmor.getGlowValue(pPackedLight));
            }
        }
    }
}

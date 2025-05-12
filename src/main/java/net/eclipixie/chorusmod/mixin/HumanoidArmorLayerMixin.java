package net.eclipixie.chorusmod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.eclipixie.chorusmod.client.render.ModRenderTypes;
import net.eclipixie.chorusmod.item.ModArmorMaterials;
import net.eclipixie.chorusmod.item.custom.ArmorSetCore;
import net.eclipixie.chorusmod.item.custom.armor.SculkExoskeletonArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Shadow
    public ResourceLocation getArmorResource(net.minecraft.world.entity.Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type)
    { return null; }

    @Unique
    private void chorus$renderTranslucentEmissiveModel(PoseStack pPoseStack, MultiBufferSource pBuffer, net.minecraft.client.model.Model pModel, float pRed, float pGreen, float pBlue, float pAlpha, ResourceLocation armorResource) {
        chorus$renderTranslucentModel(pPoseStack, pBuffer,
                LightTexture.pack(15, 15), pModel,
                pRed, pGreen, pBlue, pAlpha, armorResource);
    }

    @Unique
    private void chorus$renderTranslucentModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel, float pRed, float pGreen, float pBlue, float pAlpha, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(ModRenderTypes.translucentArmor(armorResource));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight,
                OverlayTexture.NO_OVERLAY, pRed, pGreen, pBlue, pAlpha);
    }

    @Shadow
    protected void setPartVisibility(A pModel, EquipmentSlot pSlot)
    { }

    @Shadow
    protected net.minecraft.client.model.Model getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model)
    { return null; }

    @Inject(method = "renderArmorPiece", at = @At("RETURN"))
    private void invokeRenderArmorPiece(PoseStack pPoseStack, MultiBufferSource pBuffer, T pLivingEntity, EquipmentSlot pSlot, int pPackedLight, A pModel, CallbackInfo ci) {
        if (ArmorSetCore.checkSet(ModArmorMaterials.SCULK_EXOSKELETON, pLivingEntity)) {
            ItemStack itemstack = pLivingEntity.getItemBySlot(pSlot);
            SculkExoskeletonArmor armoritem = (SculkExoskeletonArmor) itemstack.getItem();

            this.getParentModel().copyPropertiesTo(pModel);
            this.setPartVisibility(pModel, pSlot);
            net.minecraft.client.model.Model model = getArmorModelHook(pLivingEntity, itemstack, pSlot, pModel);

            this.chorus$renderTranslucentEmissiveModel(pPoseStack, pBuffer, model,
                    1.0F, 1.0F, 1.0F, armoritem.getGlowValue(pPackedLight),
                    this.getArmorResource(pLivingEntity, itemstack, pSlot, "emissive"));
        }
    }
}

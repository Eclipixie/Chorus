package net.eclipixie.chorusmod.entity.client.renderer;

import net.eclipixie.chorusmod.ChorusMod;
import net.eclipixie.chorusmod.entity.client.model.SculkExoskeletonModel3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class SculkExoskeletonRenderer<L extends LivingEntity> extends LivingEntityRenderer<L, SculkExoskeletonModel3<L>> {
    public SculkExoskeletonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext,
                new SculkExoskeletonModel3<>(
                        pContext.bakeLayer(SculkExoskeletonModel3.LAYER_LOCATION)
                ), .5f);
    }

    @Override
    public ResourceLocation getTextureLocation(L pEntity) {
        return new ResourceLocation(ChorusMod.MOD_ID,
                "textures/entity/armor/sculk_exoskeleton_3_2d.png");
    }
}

package net.eclipixie.chorusmod.entity.client.model;
// from https://github.com/AlexModGuy/Ice_and_Fire/blob/1.18.2/src/main/java/com/github/alexthe666/iceandfire/client/model/armor/ArmorModelBase.java#

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;


public class ArmorModelBase extends HumanoidModel<LivingEntity> {
    public ArmorModelBase(ModelPart part) {
        super(part);
    }

    @Override
    public void setupAnim(@NotNull LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entityIn;
            this.head.xRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getX();
            this.head.yRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getY();
            this.head.zRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getZ();
            this.body.xRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getX();
            this.body.yRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getY();
            this.body.zRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getZ();
            this.leftArm.xRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getX();
            this.leftArm.yRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getY();
            this.leftArm.zRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getZ();
            this.rightArm.xRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getX();
            this.rightArm.yRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getY();
            this.rightArm.zRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getZ();
            this.leftLeg.xRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getX();
            this.leftLeg.yRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getY();
            this.leftLeg.zRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getZ();
            this.rightLeg.xRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getX();
            this.rightLeg.yRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getY();
            this.rightLeg.zRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getZ();
            this.hat.copyFrom(this.head);
        } else {
            super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
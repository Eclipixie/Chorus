package net.eclipixie.chorusmod.mixin;


import net.eclipixie.chorusmod.util.ModTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * @author Eclipixie
     * @reason Required for custom spyglasses
     */
    @Inject(method = "isScoping", at = @At("HEAD"), cancellable = true)
    public void headIsScoping(CallbackInfoReturnable<Boolean> ci) {
        // modified: checks tag rather than item
        ci.setReturnValue(this.isUsingItem() &&
                this.getUseItem().is(ModTags.Items.IS_SPYGLASS));
    }
}

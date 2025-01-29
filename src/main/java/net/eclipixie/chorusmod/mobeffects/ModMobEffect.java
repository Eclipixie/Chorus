package net.eclipixie.chorusmod.mobeffects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// needed since MobEffect is protected for some fucking reason
public class ModMobEffect extends MobEffect {
    protected ModMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
}

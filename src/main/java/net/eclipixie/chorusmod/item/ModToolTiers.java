package net.eclipixie.chorusmod.item;

import net.eclipixie.chorusmod.ChorusMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier SCULK = TierSortingRegistry.registerTier(
            new ForgeTier(4, 1500, 8f, 5, 0,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.ECHO_SHARD)),
            ResourceLocation.fromNamespaceAndPath(ChorusMod.MOD_ID, "sculk"), List.of(Tiers.DIAMOND), List.of()
    );
}

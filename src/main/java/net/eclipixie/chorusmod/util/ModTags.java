package net.eclipixie.chorusmod.util;

import net.eclipixie.chorusmod.ChorusMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> IS_ORE = tag("is_ore");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(ChorusMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> IS_SPYGLASS = tag("is_spyglass");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(ChorusMod.MOD_ID, name));
        }
    }
}

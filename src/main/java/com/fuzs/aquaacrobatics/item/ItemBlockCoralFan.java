package com.fuzs.aquaacrobatics.item;

import com.fuzs.aquaacrobatics.block.coral.BlockAbstractCoral;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCoralFan extends ItemBlock {
    public ItemBlockCoralFan(BlockAbstractCoral block) {
        super(block);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        IBlockState state = block.getStateFromMeta(stack.getItemDamage());
        BlockAbstractCoral coral = (BlockAbstractCoral)block;
        StringBuilder sb = new StringBuilder();
        sb.append("tile.aquaacrobatics.");
        if(coral.isDead(state))
            sb.append("dead_");
        sb.append(coral.getCoralType(state).getName()).append("_coral_fan");
        return sb.toString();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}

package com.fuzs.aquaacrobatics.item;

import com.fuzs.aquaacrobatics.block.coral.BlockCoral;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCoral extends ItemBlock {
    public ItemBlockCoral(Block block) {
        super(block);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        IBlockState state = block.getStateFromMeta(stack.getItemDamage());
        String base = super.getTranslationKey(stack) + "." + state.getValue(BlockCoral.VARIANT);
        if(state.getValue(BlockCoral.DEAD))
            return base + ".dead";
        else
            return base;
    }
}

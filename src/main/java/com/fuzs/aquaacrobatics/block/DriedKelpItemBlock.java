package com.fuzs.aquaacrobatics.block;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;

public class DriedKelpItemBlock extends ItemBlock {
    public DriedKelpItemBlock() {
        super(CommonProxy.blockDriedKelp);
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 200;
    }
}

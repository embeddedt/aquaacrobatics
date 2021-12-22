package com.fuzs.aquaacrobatics.block;

import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class DriedKelpBlock extends Block {
    public DriedKelpBlock() {
        super(Material.GRASS);
        this.setHardness(0.5f);
        this.setResistance(2.5f);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return CommonProxy.itemDriedKelpBlock;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(CommonProxy.itemDriedKelpBlock);
    }
}

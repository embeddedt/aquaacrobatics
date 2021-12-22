package com.fuzs.aquaacrobatics.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;

import java.util.Random;

import static net.minecraft.block.BlockLiquid.LEVEL;

public class KelpBlock extends UnderwaterPlantBlock {
    public KelpBlock() {
        setRegistryName("kelp_plant");
    }
    
    public void initModelOverride() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
    }


    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.isValidPosition(worldIn, pos)) {
            UnderwaterPlantBlock.destroyBlockToWater(worldIn, pos, true);
            return;
        }
        Block above = worldIn.getBlockState(pos.up()).getBlock();
        if(above != CommonProxy.blockKelp && above != CommonProxy.blockKelpPlant) {
            worldIn.setBlockState(pos, CommonProxy.blockKelp.randomAge(worldIn.rand));
        }
    }
    
    public boolean isAboveValid(IBlockAccess worldIn, BlockPos pos) {
        Block above = worldIn.getBlockState(pos.up()).getBlock();
        if(above != Blocks.WATER && above != Blocks.AIR && above != Blocks.FLOWING_WATER && above != CommonProxy.blockKelpPlant && above != CommonProxy.blockKelp) {
            return false;
        }
        return true;
    }
    
    public boolean isValidPosition(IBlockAccess worldIn, BlockPos pos) {
        if(!isAboveValid(worldIn, pos))
            return false;
        BlockPos blockpos = pos.down();
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        return block != Blocks.MAGMA && (block == this || iblockstate.getBlockFaceShape(worldIn, blockpos, EnumFacing.UP) == BlockFaceShape.SOLID);
    }
    
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(CommonProxy.itemKelp);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return CommonProxy.itemKelp;
    }

}

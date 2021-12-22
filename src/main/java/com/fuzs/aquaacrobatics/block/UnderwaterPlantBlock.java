package com.fuzs.aquaacrobatics.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.block.BlockLiquid.LEVEL;

public class UnderwaterPlantBlock extends Block {
    protected UnderwaterPlantBlock() {
        super(Material.WATER);
        this.setDefaultState(this.getDefaultState().withProperty(LEVEL, 15));
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
    }
    // not opaque
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    // not full cube
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return NULL_AABB;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LEVEL);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    public static boolean destroyBlockToWater(World world, BlockPos pos, boolean dropBlock) {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block.isAir(iblockstate, world, pos))
        {
            return false;
        }
        else
        {
            world.playEvent(2001, pos, Block.getStateId(iblockstate));

            if (dropBlock)
            {
                block.dropBlockAsItem(world, pos, iblockstate, 0);
            }

            return world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        this.onBlockHarvested(world, pos, state, player);
        return world.setBlockState(pos, net.minecraft.init.Blocks.WATER.getDefaultState(), world.isRemote ? 11 : 3);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        Block b = worldIn.getBlockState(pos.up()).getBlock();
        return super.canPlaceBlockAt(worldIn, pos) && (b == Blocks.WATER || b == Blocks.FLOWING_WATER);
    }
}

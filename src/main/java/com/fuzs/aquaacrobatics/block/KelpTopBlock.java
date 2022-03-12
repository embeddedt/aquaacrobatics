package com.fuzs.aquaacrobatics.block;

import static net.minecraft.block.BlockLiquid.LEVEL;

import git.jbredwards.fluidlogged_api.common.util.FluidState;
import git.jbredwards.fluidlogged_api.common.util.FluidloggedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Random;

public class KelpTopBlock extends UnderwaterPlantBlock {
    public static final PropertyInteger AGE = PropertyInteger.create("age",0, 15);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public KelpTopBlock() {
        super();
        this.setSoundType(SoundType.SAND);
        this.setDefaultState(this.getDefaultState().withProperty(AGE, 0));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB_BOTTOM_HALF;
    }
    
    public IBlockState randomAge(Random rand) {
        return getDefaultState().withProperty(AGE, rand.nextInt(15));
    }

    /*
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return randomAge(world.rand);
    }
    
     */

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(AGE).build();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, meta);
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(AGE);
    }
    
    public void initModelOverride() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).ignore(AGE).build());
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.isValidPosition(worldIn, pos)) {
            System.out.println("top block not valid " + pos + " " + worldIn.getBlockState(pos.down()).getBlock());
            //worldIn.destroyBlock(pos, false);
            return;
        }
        if(worldIn.getBlockState(pos.up()).getBlock() == CommonProxy.blockKelp) {
            worldIn.setBlockState(pos, CommonProxy.blockKelpPlant.getDefaultState());
        }
    }

    public boolean isValidPosition(IBlockAccess worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block == Blocks.MAGMA) {
            return false;
        } else {
            boolean isValidBelow = block == this
                    || block == CommonProxy.blockKelpPlant
                    || block == CommonProxy.blockKelp
                    || iblockstate.getBlockFaceShape(worldIn, blockpos, EnumFacing.UP) == BlockFaceShape.SOLID;
            return isValidBelow;
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if(true || worldIn.isRemote)
            return;
        if (!isValidPosition(worldIn, pos)) {
            System.out.println("NOT VALID");
            worldIn.destroyBlock(pos, false);
        } else {
            if (state.getValue(AGE) < 14 && random.nextDouble() < 0.14D) {
                System.out.println("AGING! " + pos);
                worldIn.setBlockState(pos, state.withProperty(AGE,state.getValue(AGE) + 1));
            } else if(state.getValue(AGE) == 14) {
                BlockPos above = pos.up();
                if(isValidPosition(worldIn, above)) {
                    System.out.println("GROWING!");
                    worldIn.setBlockState(above, state.withProperty(AGE, 0));
                    worldIn.setBlockState(pos, CommonProxy.blockKelpPlant.getDefaultState());
                }
            }
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(CommonProxy.itemKelp);
    }
}

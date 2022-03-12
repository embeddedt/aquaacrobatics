package com.fuzs.aquaacrobatics.block;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;

import java.util.Random;

import static net.minecraft.block.BlockLiquid.LEVEL;

public class SeagrassBlock extends UnderwaterPlantBlock {
    public static enum SeagrassType implements IStringSerializable {
        SINGLE("single"),
        LOWER("lower"),
        UPPER("upper");

        private final String name;

        private SeagrassType(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
    
    public static final PropertyEnum<SeagrassType> TYPE = PropertyEnum.create("type", SeagrassType.class);
    
    public SeagrassBlock() {
        super();
        this.setDefaultState(this.getDefaultState().withProperty(TYPE, SeagrassType.SINGLE));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LEVEL, TYPE);
    }

    public boolean isValidPosition(IBlockAccess worldIn, BlockPos pos) {
        Block above = worldIn.getBlockState(pos.up()).getBlock();
        if(above != Blocks.WATER && above != Blocks.AIR && above != Blocks.FLOWING_WATER && above != this) {
            return false;
        }
        BlockPos blockpos = pos.down();
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block == Blocks.MAGMA) {
            return false;
        } else {
            return block == this
                    || iblockstate.getBlockFaceShape(worldIn, blockpos, EnumFacing.UP) == BlockFaceShape.SOLID;
        }
    }

    
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!isValidPosition(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return;
        }
        if(state.getValue(TYPE) != SeagrassType.UPPER) {
            IBlockState above = worldIn.getBlockState(pos.up());
            worldIn.setBlockState(pos, state.withProperty(TYPE, above.getBlock() == this ? SeagrassType.LOWER : SeagrassType.SINGLE));
        }
    }
    
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(TYPE) == SeagrassType.LOWER) {
            return Items.AIR;
        }
        return CommonProxy.itemSeagrass;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if(world.getBlockState(pos.down()).getBlock() == this)
            return getDefaultState().withProperty(TYPE, SeagrassType.UPPER);
        else
            return getDefaultState();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        SeagrassType type;
        switch(meta) {
            case 0:
                type = SeagrassType.SINGLE;
                break;
            case 1:
                type = SeagrassType.LOWER;
                break;
            case 2:
                type = SeagrassType.UPPER;
                break;
            default:
                return this.getDefaultState();
        }
        return this.getDefaultState().withProperty(TYPE, type);
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        switch(state.getValue(TYPE)) {
            case SINGLE:
                return 0;
            case LOWER:
                return 1;
            case UPPER:
                return 2;
            default:
                return 0;
        }
    }
    
    public void initModelOverride() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState below = worldIn.getBlockState(pos.down());
        if(below.getBlock() == this) {
            if(below.getValue(TYPE) == SeagrassType.UPPER)
                return false;
        }
        return super.canPlaceBlockAt(worldIn, pos) && isValidPosition(worldIn, pos);
    }
}

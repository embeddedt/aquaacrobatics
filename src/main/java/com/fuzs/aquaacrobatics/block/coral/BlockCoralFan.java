package com.fuzs.aquaacrobatics.block.coral;

import com.fuzs.aquaacrobatics.util.CompatVoxelShape;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class BlockCoralFan extends BlockAbstractUnderwaterCoral {
    private static final AxisAlignedBB GROUND_SHAPE = new AxisAlignedBB(2D/16D, 0D, 2D/16D, 14D/16D, 4D/16D, 14D/16D);
    private static final Map<EnumFacing, AxisAlignedBB> SHAPES = Maps.newEnumMap(ImmutableMap.of(EnumFacing.NORTH, CompatVoxelShape.makeCuboidShape(0.0D, 4.0D, 5.0D, 16.0D, 12.0D, 16.0D), EnumFacing.SOUTH, CompatVoxelShape.makeCuboidShape(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 11.0D), EnumFacing.WEST, CompatVoxelShape.makeCuboidShape(5.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D), EnumFacing.EAST, CompatVoxelShape.makeCuboidShape(0.0D, 4.0D, 0.0D, 11.0D, 12.0D, 16.0D)));
    private final BlockCoral.EnumType type;
    public static final PropertyDirection FACING = PropertyDirection.create("facing", dir -> dir != EnumFacing.DOWN);
    public BlockCoralFan(BlockCoral.EnumType type) {
        this.type = type;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.UP).withProperty(BlockCoral.DEAD, false));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        return SHAPES.getOrDefault(facing, GROUND_SHAPE);
    }

    @Override
    public BlockCoral.EnumType getCoralType(IBlockState state) {
        return type;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isDead = (meta & 8) != 0;
        EnumFacing enumfacing;
        switch (meta & 7)
        {
            default:
            case 0:
                enumfacing = EnumFacing.UP;
                break;
            case 1:
                enumfacing = EnumFacing.EAST;
                break;
            case 2:
                enumfacing = EnumFacing.WEST;
                break;
            case 3:
                enumfacing = EnumFacing.SOUTH;
                break;
            case 4:
                enumfacing = EnumFacing.NORTH;
                break;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(BlockCoral.DEAD, isDead);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta;

        switch (state.getValue(FACING))
        {
            case EAST:
                meta = 1;
                break;
            case WEST:
                meta = 2;
                break;
            case SOUTH:
                meta = 3;
                break;
            case NORTH:
                meta = 4;
                break;
            case UP:
            default:
                meta = 0;
                break;
        }
        if(state.getValue(BlockCoral.DEAD))
            meta |= 8;
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(BlockCoral.DEAD, FACING).build();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 8));
    }

    @Override
    public IBlockState getDeadVersion(World worldIn, BlockPos pos, IBlockState current) {
        return current.withProperty(BlockCoral.DEAD, true);
    }

    @Override
    public boolean isDead(IBlockState state) {
        return state.getValue(BlockCoral.DEAD);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

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
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing finalFacing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if(finalFacing == EnumFacing.UP || finalFacing.getAxis().isHorizontal())
            return this.getDefaultState().withProperty(FACING, finalFacing);
        return this.getDefaultState();
    }
}

package com.fuzs.aquaacrobatics.block.coral;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.fuzs.aquaacrobatics.block.coral.BlockCoral.DEAD;
import static com.fuzs.aquaacrobatics.block.coral.BlockCoral.VARIANT;

public class BlockCoralPlant extends BlockAbstractUnderwaterCoral {
    public BlockCoralPlant() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public IBlockState getDeadVersion(World worldIn, BlockPos pos, IBlockState current) {
        return current.withProperty(DEAD, true);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(DEAD, VARIANT).build();
    }

    @Override
    public boolean isDead(IBlockState state) {
        return state.getValue(DEAD);
    }

    @Override
    public BlockCoral.EnumType getCoralType(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = (state.getValue(VARIANT).getMeta() << 1);
        if(state.getValue(DEAD))
            meta |= 1;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        BlockCoral.EnumType type = BlockCoral.EnumType.byMeta(meta >> 1);
        return this.getDefaultState().withProperty(VARIANT, type).withProperty(DEAD, (meta & 1) != 0);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for(IBlockState state : this.getBlockState().getValidStates()) {
            items.add(new ItemStack(this, 1, this.getMetaFromState(state)));
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, this.getMetaFromState(state));
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
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}

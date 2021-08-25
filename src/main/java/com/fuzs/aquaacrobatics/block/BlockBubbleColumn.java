package com.fuzs.aquaacrobatics.block;

import java.util.Random;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBubbleColumn extends BlockStaticLiquid {
   public static final PropertyBool DRAG = PropertyBool.create("drag"); /* true: upwards, false: downwards */

   public BlockBubbleColumn() {
      super(Material.WATER);
      this.setRegistryName("bubble_column");
      this.setUnlocalizedName("Bubble Column"); /* not translating this as no one should see it */
      this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0).withProperty(DRAG, false));
   }

   @Override
   protected BlockStateContainer createBlockState()
   {
      return new BlockStateContainer(this, LEVEL, DRAG);
   }
   
   public void onEnterBubbleColumn(IBlockState state, Entity entityIn) {
      if(entityIn instanceof EntityBoat)
         return;
      if(state.getValue(DRAG)) {
         entityIn.motionY = Math.min(0.7, entityIn.motionY + 0.06);
      } else
         entityIn.motionY = Math.max(-0.3, entityIn.motionY - 0.03);
      entityIn.fallDistance = 0.0F;
   }

   public void onEnterBubbleColumnWithAirAbove(IBlockState state, Entity entityIn) {
      if(entityIn instanceof EntityBoat)
         return;
      if(state.getValue(DRAG)) {
         entityIn.motionY = Math.min(1.8, entityIn.motionY + 0.1);
      } else
         entityIn.motionY = Math.max(-0.9, entityIn.motionY - 0.03);
   }

   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      IBlockState iblockstate = worldIn.getBlockState(pos.up());
      if (iblockstate.getBlock() == Blocks.AIR) {
         this.onEnterBubbleColumnWithAirAbove(state, entityIn);
      } else {
         this.onEnterBubbleColumn(state, entityIn);
      }

   }

   public static void placeBubbleColumn(World world, BlockPos pos, boolean isUpwards) {
      if(!ConfigHandler.MiscellaneousConfig.bubbleColumns)
         return;
      if (canHoldBubbleColumn(world, pos, isUpwards)) {
         world.setBlockState(pos, CommonProxy.BUBBLE_COLUMN.getDefaultState().withProperty(DRAG, isUpwards));
      }
   }

   public static boolean canHoldBubbleColumn(World world, BlockPos pos, boolean isUpwards) {
      if(world.provider.doesWaterVaporize())
         return false;
      IBlockState self = world.getBlockState(pos);
      if(self.getMaterial() != Material.WATER)
         return false;
      if(!(self.getBlock() instanceof BlockLiquid))
         return false;
      if(self.getValue(LEVEL) != 0)
         return false;
      IBlockState below = world.getBlockState(pos.down());
      if(below.getBlock() == (isUpwards ? Blocks.SOUL_SAND : Blocks.MAGMA))
         return true;
      if(below.getBlock() instanceof BlockBubbleColumn && below.getValue(DRAG) == isUpwards)
         return true;
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
      double d0 = (double)pos.getX();
      double d1 = (double)pos.getY();
      double d2 = (double)pos.getZ();
      double sign = stateIn.getValue(DRAG) ? 1 : -1;
      worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0 + 0.5, d1, d2 + 0.5, 0.0, sign, 0.0, new int[0]);
      worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0, sign, 0.0, new int[0]);
      worldIn.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0 + (double)rand.nextFloat(), d1 + (double)rand.nextFloat(), d2 + (double)rand.nextFloat(), 0.0, sign, 0.0, new int[0]);
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
      if (!this.isValidPosition(worldIn, pos)) {
         worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
         return;
      }
      super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
      placeBubbleColumn(worldIn, pos.up(), state.getValue(DRAG));
   }

   public boolean isValidPosition(World worldIn, BlockPos pos) {
      Block block = worldIn.getBlockState(pos.down()).getBlock();
      return block == this || block == (worldIn.getBlockState(pos).getValue(DRAG) ? Blocks.SOUL_SAND : Blocks.MAGMA);
   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      super.onBlockAdded(worldIn, pos, state);
      placeBubbleColumn(worldIn, pos.up(), state.getValue(DRAG));
   }

   public IBlockState getStateFromMeta(int meta)
   {
      return this.getDefaultState().withProperty(LEVEL, 0).withProperty(DRAG, (meta & 1) == 1);
   }

   public int getMetaFromState(IBlockState state)
   {
      return state.getValue(DRAG) ? 1 : 0;
   }
}

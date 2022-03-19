package com.fuzs.aquaacrobatics.world.structure;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

import static com.fuzs.aquaacrobatics.world.gen.FluidBlockHelper.isLiquid;

/**
 * Based on MCP 1.13.
 */
public class BuriedTreasurePieces {
    public static void registerBuriedTreasurePieces() {
        MapGenStructureIO.registerStructureComponent(BuriedTreasurePieces.Piece.class, "BTP");
    }

    public static class Piece extends StructureComponent {
        private boolean isStone(IBlockState self) {
            if(self.getBlock() != Blocks.STONE)
                return false;
            return self.getValue(BlockStone.VARIANT).isNatural();
        }
        public Piece() {
        }
        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound) {

        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {

        }

        public Piece(BlockPos pos) {
            super(0);
            this.boundingBox = new StructureBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }

        public boolean addComponentParts(World world, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            int i = 90; //worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, this.boundingBox.minX, this.boundingBox.minZ);
            BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(this.boundingBox.minX, i, this.boundingBox.minZ);

            while(blockPos.getY() > 0) {
                IBlockState self = world.getBlockState(blockPos);
                IBlockState below = world.getBlockState(blockPos.down());
                if (self == Blocks.SANDSTONE.getDefaultState() || isStone(self)) {
                    IBlockState coverBlock = !world.isAirBlock(blockPos) && !isLiquid(world, blockPos) ? self : Blocks.SAND.getDefaultState();
                    for(EnumFacing enumfacing : EnumFacing.values()) {
                        BlockPos sideBlockPos = blockPos.offset(enumfacing);
                        if (world.isAirBlock(sideBlockPos) || isLiquid(world, sideBlockPos)) {
                            BlockPos sideBelow = sideBlockPos.down();
                            if ((world.isAirBlock(sideBelow) || isLiquid(world, sideBelow)) && enumfacing != EnumFacing.UP) {
                                world.setBlockState(sideBlockPos, below, 3);
                            } else {
                                world.setBlockState(sideBlockPos, coverBlock, 3);
                            }
                        }
                    }
                    return this.generateChest(world, structureBoundingBoxIn, randomIn, new BlockPos(this.boundingBox.minX, blockPos.getY(), this.boundingBox.minZ), new ResourceLocation(AquaAcrobatics.MODID, "chests/buried_treasure"), (IBlockState)null);
                }
                blockPos.setPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ());
            }

            return false;
        }
    }
}

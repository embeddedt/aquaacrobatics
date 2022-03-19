package com.fuzs.aquaacrobatics.world.structure;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.fuzs.aquaacrobatics.world.gen.WorldGenHandler;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;
import java.util.Random;

/**
 * Based on MCP 1.13.
 */
public class ShipwreckPieces {
    private static final BlockPos STRUCTURE_OFFSET = new BlockPos(4, 0, 15);
    private static final ResourceLocation[] BEACHED_STRUCTURES = new ResourceLocation[]{new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/with_mast"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_full"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_fronthalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_backhalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_full"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_fronthalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_backhalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/with_mast_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_full_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_fronthalf_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_backhalf_degraded")};
    private static final ResourceLocation[] UNBEACHED_STRUCTURES = new ResourceLocation[]{new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/with_mast"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/upsidedown_full"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/upsidedown_fronthalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/upsidedown_backhalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_full"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_fronthalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_backhalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_full"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_fronthalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_backhalf"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/with_mast_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/upsidedown_full_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/upsidedown_fronthalf_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/upsidedown_backhalf_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_full_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_fronthalf_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/sideways_backhalf_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_full_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_fronthalf_degraded"), new ResourceLocation(AquaAcrobatics.MODID, "shipwreck/rightsideup_backhalf_degraded")};

    public static void registerShipwreckPieces() {
        MapGenStructureIO.registerStructureComponent(ShipwreckPieces.Piece.class, "Shipwreck");
    }

    public static void createPiece(World world, BlockPos pos, Rotation rot, List<StructureComponent> partList, Random rand, boolean isBeached) {
        ResourceLocation resourcelocation = isBeached ? BEACHED_STRUCTURES[rand.nextInt(BEACHED_STRUCTURES.length)] : UNBEACHED_STRUCTURES[rand.nextInt(UNBEACHED_STRUCTURES.length)];
        partList.add(new ShipwreckPieces.Piece(resourcelocation, pos, rot, isBeached));
    }

    public static class Piece extends StructureComponentFlattenedTemplate {
        private Rotation rotation;
        private ResourceLocation structureLocation;
        private boolean isBeached;

        public Piece() {
        }

        public Piece(ResourceLocation loc, BlockPos pos, Rotation rot, boolean isBeached) {
            super(0);
            this.templatePosition = pos;
            this.rotation = rot;
            this.structureLocation = loc;
            this.isBeached = isBeached;
            this.func_204754_a();
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setString("Template", this.structureLocation.toString());
            tagCompound.setBoolean("isBeached", this.isBeached);
            tagCompound.setString("Rot", this.rotation.name());
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound, p_143011_2_);
            this.structureLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.isBeached = tagCompound.getBoolean("isBeached");
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.func_204754_a();
        }

        private void func_204754_a() {
            Template template = AquaTemplateManager.getTemplate(this.structureLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setReplacedBlock(Blocks.AIR).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb) {
            if ("map_chest".equals(function)) {
                TileEntity tileEntity = worldIn.getTileEntity(pos.down());
                if(tileEntity instanceof TileEntityChest)
                    ((TileEntityChest)tileEntity).setLootTable(new ResourceLocation(AquaAcrobatics.MODID, "chests/shipwreck_map"), rand.nextLong());
            } else if ("treasure_chest".equals(function)) {
                TileEntity tileEntity = worldIn.getTileEntity(pos.down());
                if(tileEntity instanceof TileEntityChest)
                    ((TileEntityChest)tileEntity).setLootTable(new ResourceLocation(AquaAcrobatics.MODID, "chests/shipwreck_treasure"), rand.nextLong());
            } else if ("supply_chest".equals(function)) {
                TileEntity tileEntity = worldIn.getTileEntity(pos.down());
                if(tileEntity instanceof TileEntityChest)
                    ((TileEntityChest)tileEntity).setLootTable(new ResourceLocation(AquaAcrobatics.MODID, "chests/shipwreck_supply"), rand.nextLong());
            }
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            int i = 256;
            int j = 0;
            BlockPos blockpos = this.templatePosition.add(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1);
            int seaLevel = Math.max(worldIn.getSeaLevel() - 1, 1);
            for(BlockPos blockpos1 : BlockPos.getAllInBox(this.templatePosition, blockpos)) {
                int k = this.isBeached ? worldIn.getHeight(blockpos1.getX(), blockpos1.getZ()) : WorldGenHandler.getSeaBed(worldIn, new BlockPos(blockpos1.getX(), seaLevel, blockpos1.getZ())).getY();
                j += k;
                i = Math.min(i, k);
            }

            j = j / (this.template.getSize().getX() * this.template.getSize().getZ());
            int l = this.isBeached ? i - this.template.getSize().getY() / 2 - randomIn.nextInt(3) : j;
            this.templatePosition = new BlockPos(this.templatePosition.getX(), l, this.templatePosition.getZ());
            return super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
        }
    }
}

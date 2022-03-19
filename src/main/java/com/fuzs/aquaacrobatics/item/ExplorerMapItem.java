package com.fuzs.aquaacrobatics.item;

import com.fuzs.aquaacrobatics.world.structure.BuriedTreasureStructure;
import com.fuzs.aquaacrobatics.world.structure.ShipwreckStructure;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEmptyMap;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import java.util.Locale;

public class ExplorerMapItem extends ItemEmptyMap {
    private final String structureName;
    public ExplorerMapItem(String structureName) {
        super();
        this.structureName = structureName;
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);
        if(!worldIn.isRemote) {
            ItemStack itemstack = result.getResult();
            World world = playerIn.world;
            MapGenStructure aquaStructure;
            switch(structureName) {
                case "Buried_Treasure":
                    aquaStructure = new BuriedTreasureStructure();
                    break;
                case "Shipwreck":
                    aquaStructure = new ShipwreckStructure();
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected structure: " + structureName);
            }
            BlockPos blockpos = aquaStructure.getNearestStructurePos(world, playerIn.getPosition(), true);
            if (blockpos != null)
            {
                MapData data = ((ItemMap)itemstack.getItem()).getMapData(itemstack, world);
                if(data != null) {
                    data.scale = 2;
                    data.calculateMapCenter(blockpos.getX(), blockpos.getZ(), data.scale);
                    data.unlimitedTracking = true;
                    data.markDirty();
                    worldIn.setData("map_" + itemstack.getMetadata(), data);
                }
                ItemMap.renderBiomePreviewMap(world, itemstack);
                System.out.println(blockpos.toString());
                MapData.addTargetDecoration(itemstack, blockpos, "+", MapDecoration.Type.TARGET_X);
                itemstack.setTranslatableName("filled_map." + this.structureName.toLowerCase(Locale.ROOT));
            }
        }
        return result;
    }
}

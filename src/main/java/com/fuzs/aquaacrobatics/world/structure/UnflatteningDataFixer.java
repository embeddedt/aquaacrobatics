package com.fuzs.aquaacrobatics.world.structure;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class UnflatteningDataFixer {
    private static Map<String, IBlockState> blockNamesMap;

    /*
    public static void putMapping(String path, IBlockState state) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Name", state.getBlock().getRegistryName().toString());
        IBlockState def = state.getBlock().getDefaultState();
        NBTTagCompound properties = null;
        for(IProperty<?> prop : state.getPropertyKeys()) {
            if(!Objects.equals(state.getValue(prop), def.getValue(prop))) {
                if(properties == null)
                    properties = new NBTTagCompound();
                properties.setString(prop.getName(), String.valueOf(state.getValue(prop)));
            }
        }
        if(properties != null)
            compound.setTag("Properties", properties);
        unflatteningMap.put(path, compound);
    }

     */
    public static void init() {
        blockNamesMap = new HashMap<>();
        //putMapping("stone", Blocks.PURPUR_BLOCK.getDefaultState());
        for(BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            blockNamesMap.put(type.getName() + "_planks", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, type));
            if(type == BlockPlanks.EnumType.ACACIA || type == BlockPlanks.EnumType.DARK_OAK) {
                blockNamesMap.put(type.getName() + "_log", Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockNewLog.VARIANT.parseValue(type.getName()).get()));
            } else {
                blockNamesMap.put(type.getName() + "_log", Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockOldLog.VARIANT.parseValue(type.getName()).get()));
            }
            blockNamesMap.put(type.getName() + "_slab", Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockPlanks.VARIANT, type));
            blockNamesMap.put(type.getName() + "_trapdoor", Blocks.TRAPDOOR.getDefaultState());
        }
        blockNamesMap.put("oak_door", Blocks.OAK_DOOR.getDefaultState());
        blockNamesMap.put("oak_fence", Blocks.OAK_FENCE.getDefaultState());
    }

    private static void translateToNewProperty(String name, String value, NBTTagCompound propertiesCompound) {

    }

    protected static NBTTagCompound translateBlock(NBTTagCompound paletteEntry) {
        String registryName = paletteEntry.getString("Name");
        ResourceLocation theLocation = new ResourceLocation(registryName);
        /* Figure out what unflattened block we'll be using first */
        IBlockState unflattened;
        if(!ForgeRegistries.BLOCKS.containsKey(theLocation)) {
            if(!theLocation.getNamespace().equals("minecraft"))
                throw new IllegalArgumentException("Cannot unflatten modded 1.13+ blocks");
            unflattened = blockNamesMap.get(theLocation.getPath());
            if(unflattened == null) {
                AquaAcrobatics.LOGGER.warn("Can't find appropriate block for: " + theLocation.getPath());
                blockNamesMap.put(theLocation.getPath(), Blocks.PURPUR_BLOCK.getDefaultState());
                unflattened = Blocks.PURPUR_BLOCK.getDefaultState();
            }
        } else
            unflattened = ForgeRegistries.BLOCKS.getValue(theLocation).getDefaultState();
        paletteEntry.setString("Name", unflattened.getBlock().getRegistryName().toString());
        /* Now attempt to translate properties (if any) */
        IBlockState defaultState = unflattened.getBlock().getDefaultState();
        NBTTagCompound newProperties = new NBTTagCompound();
        for(IProperty<?> prop : unflattened.getPropertyKeys()) {
            if(!Objects.equals(unflattened.getValue(prop), defaultState.getValue(prop))) {
                newProperties.setString(prop.getName(), String.valueOf(unflattened.getValue(prop)));
            }
        }
        NBTTagCompound oldProperties = paletteEntry.hasKey("Properties", Constants.NBT.TAG_COMPOUND) ? paletteEntry.getCompoundTag("Properties") : null;
        if(oldProperties != null) {
            for(Map.Entry<String, NBTBase> propEntry : oldProperties.tagMap.entrySet()) {
                String propName = propEntry.getKey();
                String propValue = ((NBTTagString)propEntry.getValue()).getString();
                IProperty<?> prop = unflattened.getBlock().getBlockState().getProperty(propName);
                if(prop == null) {
                    translateToNewProperty(propName, propValue, newProperties);
                } else
                    newProperties.setString(propName, propValue);
            }
        }
        if(newProperties.getSize() > 0)
            paletteEntry.setTag("Properties", newProperties);
        else if(oldProperties != null)
            paletteEntry.removeTag("Properties");
        return paletteEntry;
    }

    public static NBTTagCompound unflatten(NBTTagCompound compound) {
        NBTTagList ourPalette;
        if(compound.hasKey("palettes")) {
            NBTTagList thePalettes = compound.getTagList("palettes", Constants.NBT.TAG_LIST);
            int idx = 0; //.nextInt(thePalettes.tagCount());
            ourPalette = (NBTTagList)thePalettes.get(idx);
        } else
            ourPalette = compound.getTagList("palette", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < ourPalette.tagCount(); i++) {
            NBTTagCompound entry = (NBTTagCompound)ourPalette.get(i);
            ourPalette.set(i, translateBlock(entry));
        }
        compound.setTag("palette", ourPalette);
        compound.setBoolean("keepOldFluidStates", true);
        return compound;
    }
}

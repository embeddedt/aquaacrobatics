package com.fuzs.aquaacrobatics.block.coral;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

/**
 * Solid coral.
 */
public class BlockCoral extends Block {
    public static final PropertyEnum<BlockCoral.EnumType> VARIANT = PropertyEnum.create("variant", BlockCoral.EnumType.class);
    public static final PropertyBool DEAD = PropertyBool.create("dead");

    public BlockCoral() {
        super(Material.ROCK);
        this.setHardness(1.5f);
        this.setResistance(6.0f);
        this.setDefaultState(this.getDefaultState().withProperty(VARIANT, EnumType.TUBE).withProperty(DEAD, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(VARIANT, DEAD).build();
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
        EnumType type = EnumType.byMeta(meta >> 1);
        return this.getDefaultState().withProperty(VARIANT, type).withProperty(DEAD, (meta & 1) != 0);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for(IBlockState state : this.getBlockState().getValidStates()) {
            items.add(new ItemStack(this, 1, this.getMetaFromState(state)));
        }
    }

    public enum EnumType implements IStringSerializable
    {
        TUBE("tube", 0),
        BRAIN("brain", 1),
        BUBBLE("bubble", 2),
        FIRE("fire", 3),
        HORN("horn", 4);

        private final String name;
        private final int meta;
        private static final BlockCoral.EnumType[] META_LOOKUP = new BlockCoral.EnumType[values().length];

        EnumType(String name, int meta) {
            this.name = name;
            this.meta = meta;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public int getMeta() {
            return this.meta;
        }

        public static EnumType byMeta(int m) {
            return META_LOOKUP[m];
        }

        static {
            for(EnumType type : values()) {
                META_LOOKUP[type.meta] = type;
            }
        }
    }
}

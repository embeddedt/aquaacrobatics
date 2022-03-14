package com.fuzs.aquaacrobatics.effect;

import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionConduitPower extends Potion {
    private final ItemStack icon = new ItemStack(CommonProxy.itemSeaHeart);
    public PotionConduitPower() {
        super(true, 1950417);
        setRegistryName("conduit_power");
        setPotionName("effect.aquaacrobatics.conduit_power.name");
        setBeneficial();
    }

    @Override
    public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(icon, x+4, y+4);
    }

    @Override
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(icon, x+8, y+8);
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }
}

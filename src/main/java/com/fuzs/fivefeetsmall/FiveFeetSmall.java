package com.fuzs.fivefeetsmall;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
        modid = FiveFeetSmall.MODID,
        name = FiveFeetSmall.NAME,
        version = FiveFeetSmall.VERSION,
        acceptedMinecraftVersions = FiveFeetSmall.RANGE,
        dependencies = FiveFeetSmall.DEPENDENCIES
)
public class FiveFeetSmall {

    public static final String MODID = "fivefeetsmall";
    public static final String NAME = "Five Feet Small";
    public static final String VERSION = "1.1";
    public static final String RANGE = "[1.12.2]";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2779,)";

    // TODO
    @Mod.EventHandler
    public void onPostInit(final FMLPostInitializationEvent evt) {

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void on(GuiOpenEvent evt) {

        System.out.println("Open gui");
        if (Minecraft.getMinecraft().player != null) {

            System.out.println(Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(Minecraft.getMinecraft().player));
        }
    }

}

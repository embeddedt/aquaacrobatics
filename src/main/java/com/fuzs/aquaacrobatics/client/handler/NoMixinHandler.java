package com.fuzs.aquaacrobatics.client.handler;

import com.fuzs.aquaacrobatics.client.gui.GuiNoMixin;
import com.fuzs.aquaacrobatics.core.AquaAcrobaticsCore;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoMixinHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent evt) {

        if (evt.getGui() instanceof GuiMainMenu) {
            if(!AquaAcrobaticsCore.isLoaded()) {
                evt.setGui(new GuiNoMixin(evt.getGui()));
            }
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

}

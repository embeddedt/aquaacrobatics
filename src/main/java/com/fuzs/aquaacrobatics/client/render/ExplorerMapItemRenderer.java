package com.fuzs.aquaacrobatics.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;

public class ExplorerMapItemRenderer {
    public static ThreadLocal<ItemStack> currentMapBeingRendered = ThreadLocal.withInitial(() -> null);
    public static void renderMap(ItemStack map) {
        MapData data = ((ItemMap)map.getItem()).getMapData(map, Minecraft.getMinecraft().world);
        if(data != null) {
            currentMapBeingRendered.set(map);
            Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().renderMap(data, false);
            currentMapBeingRendered.set(null);
        }
    }
    public static void onRenderDecorations() {
        ItemStack map = currentMapBeingRendered.get();

    }
}

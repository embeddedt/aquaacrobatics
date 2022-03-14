package com.fuzs.aquaacrobatics.client.render;

import com.fuzs.aquaacrobatics.tile.TileEntityConduit;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class TileEntityConduitItemRenderer extends TileEntityItemStackRenderer {
    private final TileEntityConduit conduit = new TileEntityConduit();
    public void renderByItem(ItemStack itemStackIn) {
        TileEntityRendererDispatcher.instance.render(this.conduit, 0.0D, 0.0D, 0.0D, 0.0F);
    }
}

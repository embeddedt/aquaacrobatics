package com.fuzs.aquaacrobatics.core.mixin.client;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.proxy.CommonProxy;
import com.fuzs.aquaacrobatics.tile.TileEntityConduit;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityItemStackRenderer.class)
public class TileEntityItemStackRendererMixin {
    private final TileEntityConduit theConduit = new TileEntityConduit();
    @Inject(method = "renderByItem(Lnet/minecraft/item/ItemStack;F)V", at = @At("HEAD"), cancellable = true)
    private void renderConduitItem(ItemStack stack, float partialTicks, CallbackInfo ci) {
        if(ConfigHandler.MiscellaneousConfig.aquaticWorldContent
                && Block.getBlockFromItem(stack.getItem()) == CommonProxy.blockConduit) {
            System.out.println("conduit");
            TileEntityRendererDispatcher.instance.render(theConduit, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
            ci.cancel();
        }
    }
}

package com.fuzs.aquaacrobatics.core.mixin;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import com.fuzs.aquaacrobatics.world.gen.WorldGenHandler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkGeneratorOverworld.class)
public class ChunkGeneratorOverworldMixin {
    @Shadow @Final private World world;

    @Inject(method = "generateChunk", at = @At(value = "NEW", args="class=net/minecraft/world/chunk/Chunk"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChunkGenerateStructures(int x, int z, CallbackInfoReturnable<Chunk> ci, ChunkPrimer primer) {
        if(ConfigHandler.MiscellaneousConfig.aquaticWorldContent) {
            WorldGenHandler.onChunkGenerate(this.world, x, z, primer);
        }
    }
}

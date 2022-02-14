package com.fuzs.aquaacrobatics.client.model;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.AbstractResourcePack;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class WaterResourcePack extends AbstractResourcePack {
    private static final Set<String> CONTENTS_FILTER = ImmutableSet.of(
            "assets/minecraft/textures/blocks/water_still.png",
            "assets/minecraft/textures/blocks/water_still.png.mcmeta",
            "assets/minecraft/textures/blocks/water_flow.png",
            "assets/minecraft/textures/blocks/water_flow.png.mcmeta",
            "assets/minecraft/textures/blocks/water_overlay.png"
    );

    public WaterResourcePack(File resourcePackFileIn) {
        super(resourcePackFileIn);
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        if(name.equals("pack.mcmeta"))
            return AquaAcrobatics.class.getResourceAsStream("/water_pack.mcmeta");
        String truePath = "/" + name.replace("minecraft", "aquaacrobatics/overrides");
        return AquaAcrobatics.class.getResourceAsStream(truePath);
    }

    @Override
    protected boolean hasResourceName(String name) {
        return name.equals("pack.mcmeta") || CONTENTS_FILTER.contains(name);
    }

    @Override
    public Set<String> getResourceDomains() {
        return ImmutableSet.of("minecraft");
    }

    @Nonnull
    @Override
    public String getPackName() {
        return "aquaacrobatics-new-water";
    }
}

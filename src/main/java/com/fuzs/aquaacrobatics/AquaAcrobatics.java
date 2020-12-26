package com.fuzs.aquaacrobatics;

import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = AquaAcrobatics.MODID,
        name = AquaAcrobatics.NAME,
        version = AquaAcrobatics.VERSION,
        acceptedMinecraftVersions = AquaAcrobatics.RANGE,
        dependencies = AquaAcrobatics.DEPENDENCIES
)
public class AquaAcrobatics {

    public static final String MODID = "aquaacrobatics";
    public static final String NAME = "Aqua Acrobatics";
    public static final String VERSION = "1.1";
    public static final String RANGE = "[1.12.2]";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2779,)";

}

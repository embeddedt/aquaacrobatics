package com.fuzs.aquaacrobatics.world.structure;

import com.fuzs.aquaacrobatics.AquaAcrobatics;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.gen.structure.template.Template;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

public class AquaTemplateManager {
    private static HashMap<ResourceLocation, Template> templateMap = new HashMap<>();
    private static Template loadTemplate(ResourceLocation path) {
        String resourcePath = "assets/" + path.getNamespace() + "/structures/" + path.getPath() + ".nbt";
        AquaAcrobatics.LOGGER.info("Loading " + resourcePath);
        InputStream stream = AquaAcrobatics.class.getClassLoader().getResourceAsStream(resourcePath);
        if(stream == null)
            throw new IllegalArgumentException("Template not found: " + resourcePath);
        NBTTagCompound nbttagcompound;
        try {
            nbttagcompound = CompressedStreamTools.readCompressed(stream);
        } catch (IOException e) {
            AquaAcrobatics.LOGGER.error("Could not read template", e);
            return null;
        }
        Template template = new Template();
        template.read(UnflatteningDataFixer.unflatten(nbttagcompound));
        return template;
    }
    public static Template getTemplate(ResourceLocation path) {
        return templateMap.computeIfAbsent(path, AquaTemplateManager::loadTemplate);
    }
}

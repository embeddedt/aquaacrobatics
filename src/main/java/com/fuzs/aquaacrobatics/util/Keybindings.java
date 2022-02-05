package com.fuzs.aquaacrobatics.util;

import com.fuzs.aquaacrobatics.config.ConfigHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keybindings
{
    public static KeyBinding forceCrawling = new KeyBinding("key.aquaacrobatics.toggle_crawling", Keyboard.KEY_C, "key.aquaacrobatics.category");

    public static void register()
    {
        if(ConfigHandler.MovementConfig.enableToggleCrawling)
            ClientRegistry.registerKeyBinding(forceCrawling);
    }
}

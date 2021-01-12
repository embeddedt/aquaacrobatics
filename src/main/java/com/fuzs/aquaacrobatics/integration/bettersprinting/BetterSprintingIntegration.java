package com.fuzs.aquaacrobatics.integration.bettersprinting;

import chylex.bettersprinting.client.ClientSettings;

public class BetterSprintingIntegration {

    public static boolean enableDoubleTap() {

        return !ClientSettings.disableMod && ClientSettings.enableDoubleTap;
    }

    public static boolean enableAllDirs() {

        return !ClientSettings.disableMod && ClientSettings.enableAllDirs;
    }

}

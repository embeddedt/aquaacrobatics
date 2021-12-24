package com.fuzs.aquaacrobatics.integration.enderio;

import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import crazypants.enderio.base.handler.darksteel.StateController;
import crazypants.enderio.base.item.darksteel.upgrade.elytra.ElytraUpgrade;

public class EnderIOIntegration {
    public static void register() {
        IntegrationManager.elytraOpenHooks.add(player -> {
            if(!StateController.isActive(player, ElytraUpgrade.INSTANCE)) {
                StateController.setActive(player, ElytraUpgrade.INSTANCE, true);
            }
        });
    }
}

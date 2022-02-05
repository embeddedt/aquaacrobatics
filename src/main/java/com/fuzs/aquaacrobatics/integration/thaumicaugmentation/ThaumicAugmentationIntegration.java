package com.fuzs.aquaacrobatics.integration.thaumicaugmentation;

import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import thecodex6824.thaumicaugmentation.client.internal.TAHooksClient;

public class ThaumicAugmentationIntegration {
    public static void register() {
        IntegrationManager.elytraOpenHooks.add(TAHooksClient::checkElytra);
    }
}

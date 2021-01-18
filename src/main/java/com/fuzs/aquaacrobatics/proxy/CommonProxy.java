package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.hats.HatsIntegration;

public class CommonProxy {

    public void onPreInit() {

        IntegrationManager.loadCompat();
    }

    public void onPostInit() {

        if (IntegrationManager.isHatsEnabled()) {

            HatsIntegration.register();
        }
    }

}

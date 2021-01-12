package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.integration.IntegrationManager;
import com.fuzs.aquaacrobatics.integration.hats.HatsIntegration;
import com.fuzs.aquaacrobatics.integration.mobends.MoBendsIntegration;

public class CommonProxy {

    public void onPreInit() {

        IntegrationManager.loadCompat();
    }

    public void onPostInit() {

        if (IntegrationManager.isMoBendsEnabled()) {

            MoBendsIntegration.register();
        }

        if (IntegrationManager.isHatsEnabled()) {

            HatsIntegration.register();
        }
    }

}

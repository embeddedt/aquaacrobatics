package com.fuzs.aquaacrobatics.proxy;

import com.fuzs.aquaacrobatics.compat.ModCompatManager;
import com.fuzs.aquaacrobatics.compat.hats.HatsCompat;
import com.fuzs.aquaacrobatics.compat.mobends.MoBendsCompat;

public class CommonProxy {

    public void onPreInit() {

        ModCompatManager.loadCompat();
    }

    public void onPostInit() {

        if (ModCompatManager.enableMoBendsCompat()) {

            MoBendsCompat.register();
        }

        if (ModCompatManager.enableHatsCompat()) {

            HatsCompat.register();
        }
    }

}

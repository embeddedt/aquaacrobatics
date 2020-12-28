package com.fuzs.aquaacrobatics.compat;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class CompatibilityManager {

    public static final String RANDOM_PATCHES_ID = "randompatches";
    public static final String MO_BENDS_ID = "mobends";
    public static final String OBFUSCATE_ID = "obfuscate";

    private static final List<String> LOADED_MODS = Lists.newArrayList();
    private static final Map<String, IModCompat> MOD_COMPAT = new HashMap<>();
    private static final Map<String, Supplier<IModCompat>> COMPAT_SUPPLIERS = new HashMap<String, Supplier<IModCompat>>() {{

        put(RANDOM_PATCHES_ID, null);
        put(MO_BENDS_ID, MoBendsCompat::new);
        put(OBFUSCATE_ID, ObfuscateCompat::new);
    }};

    public static void init() {

        COMPAT_SUPPLIERS.forEach((key, value) -> {

            if (Loader.isModLoaded(key)) {

                LOADED_MODS.add(key);
                if (value != null) {

                    MOD_COMPAT.put(key, value.get());
                }
            }
        });
    }

    public static boolean isLoaded(String mod) {

        return LOADED_MODS.contains(mod);
    }

    public static void apply(String mod, Object... data) {

        Optional.ofNullable(MOD_COMPAT.get(mod)).ifPresent(compat -> compat.apply(data));
    }

}

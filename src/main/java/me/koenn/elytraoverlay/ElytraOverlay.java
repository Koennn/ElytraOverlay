package me.koenn.elytraoverlay;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, July 2017
 */
@Mod(modid = ElytraOverlay.MOD_ID, name = ElytraOverlay.MOD_NAME, version = ElytraOverlay.VERSION)
public final class ElytraOverlay {

    public static final String MOD_ID = "elytraoverlay";
    public static final String MOD_NAME = "Elytra Overlay";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TestRenderer());
    }
}

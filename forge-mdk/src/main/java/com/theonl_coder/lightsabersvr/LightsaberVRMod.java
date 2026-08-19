package com.theonl_coder.lightsabersvr;

import net.minecraftforge.fml.common.Mod;

@Mod("lightsabersvr")
public class LightsaberVRMod {
    
    public static final String MOD_ID = "lightsabersvr";
    
    public LightsaberVRMod() {
        // Mod initialized - items registered via DeferredRegister
        System.out.println("[LightsaberVR] Mod loaded successfully!");
    }
}

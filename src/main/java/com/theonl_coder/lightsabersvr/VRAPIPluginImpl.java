package com.theonl_coder.lightsabersvr;

import net.blf02.forge.VRAPIPluginProvider;
import net.blf02.vrapi.api.IVRAPI;

/**
 * Implementation of VRAPIPluginProvider for LightsaberVR.
 * 
 * This class registers our mod with the Vivecraft VR API and receives
 * a reference to the API instance for direct access to VR features.
 * 
 * The VRAPI will call getVRAPI() when the mod is loaded in a VR environment,
 * giving us full access to controller tracking, haptic feedback, etc.
 */
public class VRAPIPluginImpl implements VRAPIPluginProvider {
    
    private static IVRAPI vraipInstance = null;
    
    /**
     * Called by VRAPI when it's ready, providing us with the API instance.
     * This gives us direct access to all VR functionality without reflection!
     */
    @Override
    public void getVRAPI(IVRAPI api) {
        vraipInstance = api;
        
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║  ✓ Vivecraft VR API Connected!        ║");
        System.out.println("╚══════════════════════════════════════╝");
        
        if (api != null) {
            try {
                System.out.println("[LightsaberVR] API Version: " + api.getVersionString());
                System.out.println("[LightsaberVR] Full VR integration active!");
            } catch (Exception e) {
                System.err.println("[LightsaberVR] Error getting API version: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get the VRAPI instance for direct access
     */
    public static IVRAPI getVRAPIInstance() {
        return vraipInstance;
    }
    
    /**
     * Check if we have a valid VRAPI connection
     */
    public static boolean isVRAPIConnected() {
        return vraipInstance != null;
    }
}

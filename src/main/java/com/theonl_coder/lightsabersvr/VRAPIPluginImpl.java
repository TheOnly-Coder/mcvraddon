package com.theonl_coder.lightsabersvr;

import net.blf02.forge.VRAPIPlugin;

/**
 * Implementation of VRAPIPlugin for lightsaber VR integration.
 * This class registers our mod with the Vivecraft VR API.
 */
public class VRAPIPluginImpl implements VRAPIPlugin {
    
    @Override
    public void onVRInitialized() {
        // Called when VR is initialized
        System.out.println("[LightsaberVR] VR initialized - Lightsabers ready for VR!");
    }
    
    @Override
    public String getModID() {
        return "lightsabersvr";
    }
    
    @Override
    public String getPluginName() {
        return "LightsaberVR Plugin";
    }
}

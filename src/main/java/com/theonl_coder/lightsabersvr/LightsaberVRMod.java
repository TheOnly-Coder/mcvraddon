package com.theonl_coder.lightsabersvr;

import com.theonl_coder.lightsabersvr.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("lightsabersvr")
public class LightsaberVRMod {
    
    public static final String MOD_ID = "lightsabersvr";
    
    public static final CreativeModeTab LIGHTSABER_TAB = CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.lightsabersvr"))
            .icon(() -> ModItems.RED_LIGHTSABER.get().getDefaultInstance())
            .build();
    
    public LightsaberVRMod() {
        // Items are auto-registered via DeferredRegister
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     LightsaberVR Mod Loaded!         ║");
        System.out.println("║  10 Lightsabers + Vivecraft VR       ║");
        System.out.println("╚══════════════════════════════════════╝");
        
        // Initialize VR integration
        try {
            com.theonl_coder.lightsabersvr.vr.VRIntegration.init();
        } catch (Exception e) {
            System.out.println("[LightsaberVR] VR init deferred (API may load later)");
        }
    }
}

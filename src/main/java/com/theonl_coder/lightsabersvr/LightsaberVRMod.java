package com.theonl_coder.lightsabersvr;

import com.theonl_coder.lightsabersvr.item.ModItems;
import com.theonl_coder.lightsabersvr.vr.VRIntegration;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.javafmlmod.FMLJavaModLoadingContext;

import java.util.stream.Collectors;

@Mod("lightsabersvr")
public class LightsaberVRMod {
    
    public static final String MOD_ID = "lightsabersvr";
    
    // Creative mode tab for lightsabers
    public static final CreativeModeTab LIGHTSABER_TAB = CreativeModeTab.builder()
            .title(net.minecraft.network.chat.Component.translatable("itemGroup.lightsabersvr"))
            .icon(() -> ModItems.RED_LIGHTSABER.get().getDefaultInstance())
            .build();
    
    public LightsaberVRMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register items
        ModItems.register(modEventBus);
        
        // Register VR integration events
        MinecraftForge.EVENT_BUS.register(new VRIntegration());
        
        // Register creative tab event
        modEventBus.addListener(this::addCreative);
        
        // Register lifecycle events
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);
        modEventBus.addListener(this::clientSetup);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        // Initialize VR integration
        VRIntegration.init();
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        // Client-side setup for VR rendering
        VRIntegration.initClient();
    }
    
    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("vrapi", "register_vr_plugin", () -> new VRAPIPluginImpl());
    }
    
    private void processIMC(final InterModProcessEvent event) {
        // Process any IMC messages
    }
    
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == LIGHTSABER_TAB) {
            event.accept(ModItems.RED_LIGHTSABER.get());
            event.accept(ModItems.BLUE_LIGHTSABER.get());
            event.accept(ModItems.GREEN_LIGHTSABER.get());
            event.accept(ModItems.PURPLE_LIGHTSABER.get());
            event.accept(ModItems.YELLOW_LIGHTSABER.get());
            event.accept(ModItems.ORANGE_LIGHTSABER.get());
            event.accept(ModItems.CYAN_LIGHTSABER.get());
            event.accept(ModItems.MAGENTA_LIGHTSABER.get());
            event.accept(ModItems.WHITE_LIGHTSABER.get());
            event.accept(ModItems.BLACK_LIGHTSABER.get());
        }
    }
}

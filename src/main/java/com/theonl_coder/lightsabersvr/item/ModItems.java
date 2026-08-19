package com.theonl_coder.lightsabersvr.item;

import com.theonl_coder.lightsabersvr.LightsaberVRMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry class for all lightsaber items.
 */
public class ModItems {
    
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, LightsaberVRMod.MOD_ID);
    
    // Lightsaber colors - each with unique properties
    public static final RegistryObject<Item> RED_LIGHTSABER = ITEMS.register("red_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0xFF0000, 10.0F, 2.0F));
    
    public static final RegistryObject<Item> BLUE_LIGHTSABER = ITEMS.register("blue_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0x0066FF, 9.0F, 1.8F));
    
    public static final RegistryObject<Item> GREEN_LIGHTSABER = ITEMS.register("green_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0x00FF00, 8.5F, 1.7F));
    
    public static final RegistryObject<Item> PURPLE_LIGHTSABER = ITEMS.register("purple_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0x9900FF, 9.5F, 1.9F));
    
    public static final RegistryObject<Item> YELLOW_LIGHTSABER = ITEMS.register("yellow_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0xFFFF00, 8.0F, 1.6F));
    
    public static final RegistryObject<Item> ORANGE_LIGHTSABER = ITEMS.register("orange_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0xFF6600, 9.0F, 1.75F));
    
    public static final RegistryObject<Item> CYAN_LIGHTSABER = ITEMS.register("cyan_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0x00FFFF, 8.5F, 1.65F));
    
    public static final RegistryObject<Item> MAGENTA_LIGHTSABER = ITEMS.register("magenta_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0xFF00FF, 10.5F, 2.1F));
    
    public static final RegistryObject<Item> WHITE_LIGHTSABER = ITEMS.register("white_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0xFFFFFF, 12.0F, 2.5F));
    
    public static final RegistryObject<Item> BLACK_LIGHTSABER = ITEMS.register("black_lightsaber",
        () -> new LightsaberItem(new Item.Properties()
            .stacksTo(1)
            .fireResistant()
            .tab(LightsaberVRMod.LIGHTSABER_TAB),
            0x222222, 15.0F, 3.0F));
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

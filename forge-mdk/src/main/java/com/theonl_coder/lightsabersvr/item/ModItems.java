package com.theonl_coder.lightsabersvr.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.theonl_coder.lightsabersvr.LightsaberVRMod;

/**
 * Registry class for all lightsaber items.
 */
public class ModItems {
    
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, LightsaberVRMod.MOD_ID);
    
    // Lightsaber colors
    public static final RegistryObject<Item> RED_LIGHTSABER = ITEMS.register("red_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0xFF0000));
    
    public static final RegistryObject<Item> BLUE_LIGHTSABER = ITEMS.register("blue_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0x0066FF));
    
    public static final RegistryObject<Item> GREEN_LIGHTSABER = ITEMS.register("green_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0x00FF00));
    
    public static final RegistryObject<Item> PURPLE_LIGHTSABER = ITEMS.register("purple_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0x9900FF));
    
    public static final RegistryObject<Item> YELLOW_LIGHTSABER = ITEMS.register("yellow_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0xFFFF00));
    
    public static final RegistryObject<Item> ORANGE_LIGHTSABER = ITEMS.register("orange_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0xFF6600));
    
    public static final RegistryObject<Item> CYAN_LIGHTSABER = ITEMS.register("cyan_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0x00FFFF));
    
    public static final RegistryObject<Item> MAGENTA_LIGHTSABER = ITEMS.register("magenta_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0xFF00FF));
    
    public static final RegistryObject<Item> WHITE_LIGHTSABER = ITEMS.register("white_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0xFFFFFF));
    
    public static final RegistryObject<Item> BLACK_LIGHTSABER = ITEMS.register("black_lightsaber",
        () -> new LightsaberItem(new Item.Properties().stacksTo(1).fireResistant(), 0x222222));
}

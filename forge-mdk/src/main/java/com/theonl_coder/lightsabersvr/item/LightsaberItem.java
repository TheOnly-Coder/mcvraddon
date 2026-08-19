package com.theonl_coder.lightsabersvr.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Custom lightsaber item with VR support.
 */
public class LightsaberItem extends Item {
    
    private final int bladeColor;
    
    public LightsaberItem(Properties properties, int bladeColor) {
        super(properties);
        this.bladeColor = bladeColor;
    }
    
    public int getBladeColor() { return bladeColor; }
}

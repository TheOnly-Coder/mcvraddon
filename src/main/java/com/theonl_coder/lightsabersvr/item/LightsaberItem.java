package com.theonl_coder.lightsabersvr.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;

/**
 * Lightsaber item with VR support.
 */
public class LightsaberItem extends Item {
    
    private final int bladeColor;
    private final float attackDamage;
    private final float attackSpeed;
    
    private boolean isActivated = false;
    private double vrReachExtension = 0.0;
    
    public LightsaberItem(Properties props, int color, float damage, float speed) {
        super(props);
        this.bladeColor = color;
        this.attackDamage = damage;
        this.attackSpeed = speed;
    }
    
    public int getBladeColor() { return bladeColor; }
    public boolean isActivated() { return isActivated; }
    public double getVRReachExtension() { return vrReachExtension; }
    public void setVRReachExtension(double ext) { this.vrReachExtension = ext; }
    
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BLOCK; }
    
    public int getUseDuration(ItemStack stack) { return 72000; }
    
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            isActivated = !isActivated;
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.5F, 1.0F);
            
            if (isActivated) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 0.3F, 1.5F);
            }
        }
        
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }
    
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity,
                              int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        
        if (!level.isClientSide || !(entity instanceof Player p)) return;
        
        if (selected && isActivated) {
            spawnParticles(level, p);
        }
    }
    
    private void spawnParticles(Level level, Player p) {
        var look = p.getLookAngle();
        var pos = p.position().add(0, p.getEyeHeight() * 0.9, 0).add(look.scale(0.7));
        
        for (int i = 0; i < 2; i++) {
            double ox = (Math.random() - 0.5) * 0.1;
            double oy = (Math.random() - 0.5) * 0.1;
            double oz = (Math.random() - 0.5) * 0.1;
            level.addParticle(ParticleTypes.END_ROD, 
                pos.x + ox, pos.y + oy, pos.z + oz,
                look.x * 0.05, look.y * 0.05, look.z * 0.05);
        }
    }
    
    public String getColorName() {
        return switch (bladeColor) {
            case 0xFF0000 -> "Red";
            case 0x0066FF -> "Blue";
            case 0x00FF00 -> "Green";
            case 0x9900FF -> "Purple";
            case 0xFFFF00 -> "Yellow";
            case 0xFF6600 -> "Orange";
            case 0x00FFFF -> "Cyan";
            case 0xFF00FF -> "Magenta";
            case 0xFFFFFF -> "White Darksaber";
            case 0x222222 -> "Black Void Saber";
            default -> "Unknown";
        };
    }
}

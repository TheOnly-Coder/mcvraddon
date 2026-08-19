package com.theonl_coder.lightsabersvr.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Custom lightsaber item with VR support.
 * Features:
 * - Color-coded blades
 * - Damage and attack speed based on color
 * - Ignites entities on hit
 * - Special VR controller tracking
 */
public class LightsaberItem extends Item {
    
    private final int bladeColor;
    private final float attackDamage;
    private final float attackSpeed;
    
    // VR-specific properties
    private boolean isActivated = false;
    private double vrControllerOffset = 0.0;
    
    public LightsaberItem(Properties properties, int bladeColor, float attackDamage, float attackSpeed) {
        super(properties);
        this.bladeColor = bladeColor;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }
    
    public int getBladeColor() {
        return bladeColor;
    }
    
    public float getAttackDamage() {
        return attackDamage;
    }
    
    public float getAttackSpeed() {
        return attackSpeed;
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000; // Max duration for blocking/holding
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        // Toggle lightsaber activation (for visual/sound effect)
        if (!level.isClientSide) {
            isActivated = !isActivated;
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_ACTIVATE.getVolume() * 0.5F,
                SoundSource.PLAYERS, 1.0F, 1.0F);
            
            if (isActivated) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 0.5F, 1.5F);
            }
        }
        
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Lightsabers deal fire damage and ignite targets
        target.setSecondsOnFire(3);
        
        // Play ignition sound on hit
        if (!attacker.level.isClientSide) {
            attacker.level.playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.SHULKER_HURT, SoundSource.PLAYERS, 0.8F, 1.2F);
        }
        
        stack.hurtAndBreak(0, attacker, (p) -> p.broadcastBreakEvent(attacker.getUsedItemHand()));
        return true;
    }
    
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        
        if (!level.isClientSide || !(entity instanceof Player)) return;
        
        Player player = (Player) entity;
        
        // Emit particles when held and activated
        if (selected && isActivated) {
            spawnLightsaberParticles(level, player);
        }
    }
    
    private void spawnLightsaberParticles(Level level, Player player) {
        Vec3 look = player.getLookAngle();
        Vec3 pos = player.position().add(0, player.getEyeHeight() * 0.9, 0)
            .add(look.scale(0.7));
        
        // Spawn colored particle effect around the blade
        for (int i = 0; i < 2; i++) {
            double offsetX = (Math.random() - 0.5) * 0.1;
            double offsetY = (Math.random() - 0.5) * 0.1;
            double offsetZ = (Math.random() - 0.5) * 0.1;
            
            level.addParticle(ParticleTypes.END_ROD,
                pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ,
                look.x * 0.1, look.y * 0.1, look.z * 0.1);
        }
    }
    
    /**
     * Check if a player is within lightsaber reach in VR space.
     * Used by VR integration to determine hit detection with controllers.
     */
    public boolean isInVRReach(Player attacker, LivingEntity target, double extendedReach) {
        Vec3 attackerPos = attacker.position();
        Vec3 targetPos = target.position();
        
        double distance = attackerPos.distanceTo(targetPos);
        double baseReach = 4.0; // Standard Minecraft reach
        double totalReach = baseReach + extendedReach + vrControllerOffset;
        
        return distance <= totalReach;
    }
    
    /**
     * Get the area affected by a lightsaber swing in VR.
     * Returns an AABB representing the sweep area of the controller.
     */
    public AABB getVRSweepArea(Vec3 position, Vec3 direction, double length, double width) {
        Vec3 endPos = position.add(direction.scale(length));
        
        double halfWidth = width / 2.0;
        return new AABB(
            Math.min(position.x, endPos.x) - halfWidth,
            Math.min(position.y, endPos.y) - halfWidth,
            Math.min(position.z, endPos.z) - halfWidth,
            Math.max(position.x, endPos.x) + halfWidth,
            Math.max(position.y, endPos.y) + halfWidth,
            Math.max(position.z, endPos.z) + halfWidth
        );
    }
    
    /**
     * Set VR controller offset for extended reach calculations.
     */
    public void setVRControllerOffset(double offset) {
        this.vrControllerOffset = offset;
    }
    
    /**
     * Activate/deactivate the lightsaber (for VR toggle).
     */
    public void setActivated(boolean activated) {
        this.isActivated = activated;
    }
    
    /**
     * Check if the lightsaber is currently active.
     */
    public boolean isActivated() {
        return isActivated;
    }
}

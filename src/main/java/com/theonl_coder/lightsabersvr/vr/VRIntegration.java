package com.theonl_coder.lightsabersvr.vr;

import com.theonl_coder.lightsabersvr.item.LightsaberItem;
import com.theonl_coder.lightsabersvr.item.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles VR integration for lightsabers using the Vivecraft VRAPI.
 * This class manages:
 * - Controller tracking and rendering
 * - VR-specific hit detection
 * - Haptic feedback for lightsaber impacts
 * - Controller-based activation
 */
public class VRIntegration {
    
    private static final Logger LOGGER = LogManager.getLogger("LightsaberVR");
    private static boolean vrApiAvailable = false;
    private static Object vrapiInstance = null;
    
    // VR state tracking
    private static boolean isInVR = false;
    private static Vec3 leftControllerPos = null;
    private static Vec3 rightControllerPos = null;
    private static Vec3 hmdPosition = null;
    
    // Haptic feedback settings
    private static final float HIT_RUMBLE_STRENGTH = 1.0F;
    private static final int HIT_RUMBLE_DURATION_MS = 100;
    private static final float SWING_RUMBLE_STRENGTH = 0.3F;
    
    /**
     * Initialize VR integration - attempts to load VRAPI
     */
    public static void init() {
        try {
            // Try to load VRAPI classes
            Class<?> vraipClass = Class.forName("net.blf02.vrapi.common.VRAPI");
            vrapiInstance = vraipClass.getMethod("getInstance").invoke(null);
            vrApiAvailable = true;
            LOGGER.info("Vivecraft VR API detected! Lightsaber VR features enabled.");
        } catch (Exception e) {
            vrApiAvailable = false;
            LOGGER.info("Vivecraft VR API not found. Running in standard mode.");
        }
    }
    
    /**
     * Client-side initialization for VR rendering
     */
    public static void initClient() {
        if (vrApiAvailable) {
            LOGGER.info("LightsaberVR client integration initialized");
        }
    }
    
    /**
     * Check if VR API is available
     */
    public static boolean isVrApiAvailable() {
        return vrApiAvailable;
    }
    
    /**
     * Check if player is currently in VR mode
     */
    public static boolean isPlayerInVR(Player player) {
        if (!vrApiAvailable || vrapiInstance == null) return false;
        
        try {
            // Attempt to get VR data through reflection
            Class<?> vraipClass = vrapiInstance.getClass();
            Object vrData = vraipClass.getMethod("getVRData").invoke(vrapiInstance);
            
            if (vrData != null) {
                // Check if VR is active
                Boolean isActive = (Boolean) vrData.getClass().getMethod("isInVR").invoke(vrData);
                return isActive != null && isActive;
            }
        } catch (Exception e) {
            // Silently fail if API calls don't work
        }
        return false;
    }
    
    /**
     * Get controller position for VR hit detection
     */
    public static Vec3 getControllerPosition(Player player, boolean rightHand) {
        if (!vrApiAvailable || !isPlayerInVR(player)) {
            return null;
        }
        
        try {
            Class<?> vraipClass = vrapiInstance.getClass();
            Object vrData = vraipClass.getMethod("getVRData").invoke(vrapiInstance);
            
            if (vrData != null) {
                // Get controller position based on hand
                Object controllerData;
                if (rightHand) {
                    controllerData = vrData.getClass().getMethod("getController0").invoke(vrData);
                } else {
                    controllerData = vrData.getClass().getMethod("getController1").invoke(vrData);
                }
                
                if (controllerData != null) {
                    double x = (Double) controllerData.getClass().getMethod("getX").invoke(controllerData);
                    double y = (Double) controllerData.getClass().getMethod("getY").invoke(controllerData);
                    double z = (Double) controllerData.getClass().getMethod("getZ").invoke(controllerData);
                    
                    return new Vec3(x, y, z);
                }
            }
        } catch (Exception e) {
            // Return null on failure
        }
        return null;
    }
    
    /**
     * Get controller direction/aim vector
     */
    public static Vec3 getControllerDirection(Player player, boolean rightHand) {
        if (!vrApiAvailable || !isPlayerInVR(player)) {
            return player.getLookAngle(); // Fallback to normal look angle
        }
        
        try {
            Class<?> vraipClass = vrapiInstance.getClass();
            Object vrData = vraipClass.getMethod("getVRData").invoke(vrapiInstance);
            
            if (vrData != null) {
                Object controllerData;
                if (rightHand) {
                    controllerData = vrData.getClass().getMethod("getController0").invoke(vrData);
                } else {
                    controllerData = vrData.getClass().getMethod("getController1").invoke(vrData);
                }
                
                if (controllerData != null) {
                    double pitch = (Double) controllerData.getClass().getMethod("getPitch").invoke(controllerData);
                    double yaw = (Double) controllerData.getClass().getMethod("getYaw").invoke(controllerData);
                    
                    // Convert to direction vector
                    double x = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
                    double y = -Math.sin(Math.toRadians(pitch));
                    double z = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
                    
                    return new Vec3(x, y, z).normalize();
                }
            }
        } catch (Exception e) {
            // Fall back to look angle
        }
        return player.getLookAngle();
    }
    
    /**
     * Trigger haptic feedback on VR controllers
     */
    public static void triggerHapticFeedback(Player player, boolean rightHand, 
                                             float strength, int durationMs) {
        if (!vrApiAvailable || !isPlayerInVR(player)) return;
        
        try {
            Class<?> vraipClass = vrapiInstance.getClass();
            
            // Call rumble method if available
            try {
                vraipClass.getMethod("triggerHapticFeedback", Player.class, boolean.class, float.class, int.class)
                    .invoke(vrapiInstance, player, rightHand, strength, durationMs);
            } catch (NoSuchMethodException e) {
                // Try alternative method name
                try {
                    vraipClass.getMethod("rumbleController", Player.class, boolean.class, float.class, int.class)
                        .invoke(vrapiInstance, player, rightHand, strength, durationMs);
                } catch (NoSuchMethodException e2) {
                    LOGGER.debug("Haptic feedback method not found in VRAPI");
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Failed to trigger haptic feedback: " + e.getMessage());
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Player player = event.player;
        
        // Update VR state each tick
        updateVRState(player);
        
        // Handle VR-specific lightsaber behavior
        if (isVrApiAvailable() && isPlayerInVR(player)) {
            handleVRBehavior(player);
        }
    }
    
    private void updateVRState(Player player) {
        if (!vrApiAvailable) return;
        
        isInVR = isPlayerInVR(player);
        
        if (isInVR) {
            rightControllerPos = getControllerPosition(player, true);
            leftControllerPos = getControllerPosition(player, false);
        }
    }
    
    private void handleVRBehavior(Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offhandItem = player.getOffhandItem();
        
        // Apply VR reach extension to lightsabers
        if (mainHandItem.getItem() instanceof LightsaberItem lightsaber) {
            // Extend reach when in VR for better gameplay feel
            lightsaber.setVRControllerOffset(2.0); // 2 block extended reach in VR
        }
        
        if (offhandItem.getItem() instanceof LightsaberItem lightsaber) {
            lightsaber.setVRControllerOffset(2.0);
        }
    }
    
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        
        if (!(item.getItem() instanceof LightsaberItem)) return;
        
        // In VR, provide stronger haptic feedback on activation
        if (isVrApiAvailable() && isPlayerInVR(player)) {
            triggerHapticFeedback(player, true, 0.7F, 150);
        }
    }
    
    /**
     * Called when a lightsaber hits something in VR to provide impact feedback
     */
    public static void onLightsaberHit(Player attacker, LivingEntity target) {
        if (isVrApiAvailable && isPlayerInVR(attacker)) {
            // Determine which hand has the lightsaber
            boolean rightHand = attacker.getMainHandItem().getItem() instanceof LightsaberItem;
            
            // Strong impact rumble
            triggerHapticFeedback(attacker, rightHand, HIT_RUMBLE_STRENGTH, HIT_RUMBLE_DURATION_MS);
        }
    }
    
    /**
     * Called during lightsaber swing for subtle feedback
     */
    public static void onLightsaberSwing(Player player) {
        if (isVrApiAvailable && isPlayerInVR(player)) {
            boolean rightHand = player.getMainHandItem().getItem() instanceof LightsaberItem;
            triggerHapticFeedback(player, rightHand, SWING_RUMBLE_STRENGTH, 50);
        }
    }
    
    /**
     * Simple 3D vector class for VR positions
     */
    public static class Vec3 {
        public final double x, y, z;
        
        public Vec3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public Vec3 add(Vec3 other) {
            return new Vec3(x + other.x, y + other.y, z + other.z);
        }
        
        public Vec3 scale(double factor) {
            return new Vec3(x * factor, y * factor, z * factor);
        }
        
        public Vec3 normalize() {
            double length = Math.sqrt(x*x + y*y + z*z);
            if (length == 0) return new Vec3(0, 0, 0);
            return new Vec3(x / length, y / length, z / length);
        }
        
        public double distanceTo(Vec3 other) {
            double dx = x - other.x;
            double dy = y - other.y;
            double dz = z - other.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }
}

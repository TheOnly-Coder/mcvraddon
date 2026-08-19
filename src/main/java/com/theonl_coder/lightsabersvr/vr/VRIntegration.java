package com.theonl_coder.lightsabersvr.vr;

import com.theonl_coder.lightsabersvr.item.LightsaberItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * VR Integration for LightsaberVR using Vivecraft VRAPI.
 * 
 * This class provides runtime VR support through reflection-based API access.
 * All VR features gracefully degrade if VRAPI is not installed.
 */
public class VRIntegration {
    
    private static boolean initialized = false;
    private static boolean vrApiAvailable = false;
    private static Object vraipInstance = null;
    
    // VR state cache
    private static boolean inVR = false;
    
    // Haptic settings
    public static final float HIT_RUMBLE = 1.0F;
    public static final float SWING_RUMBLE = 0.4F;
    public static final float ACTIVATE_RUMBLE = 0.7F;
    
    /**
     * Initialize VR integration (safe to call multiple times)
     */
    public static void init() {
        if (initialized) return;
        
        try {
            Class<?> vraipClass = Class.forName("net.blf02.vrapi.common.VRAPI");
            java.lang.reflect.Field instanceField = vraipClass.getField("VRAPIInstance");
            vraipInstance = instanceField.get(null);
            
            if (vraipInstance != null) {
                vrApiAvailable = true;
                System.out.println("[LightsaberVR] ✓ Vivecraft VRAPI connected!");
                
                try {
                    java.lang.reflect.Method getVersion = vraipInstance.getClass().getMethod("getVersionString");
                    System.out.println("[LightsaberVR]   Version: " + getVersion.invoke(vraipInstance));
                } catch (Exception ignored) {}
            }
        } catch (ClassNotFoundException e) {
            System.out.println("[LightsaberVR] ℹ VRAPI not present - standard mode");
        } catch (Exception e) {
            System.err.println("[LightsaberVR] ✗ VRAPI load failed: " + e.getMessage());
        }
        
        initialized = true;
    }
    
    public static boolean isVrApiAvailable() { return vrApiAvailable && vraipInstance != null; }
    
    /**
     * Check if player is in VR mode
     */
    public static boolean isPlayerInVR(Player player) {
        if (!isVrApiAvailable()) return false;
        
        try {
            java.lang.reflect.Method m = vraipInstance.getClass().getMethod("playerInVR", Player.class);
            Boolean result = (Boolean) m.invoke(vraipInstance, player);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get controller position (right hand = true)
     */
    public static Vec3 getControllerPos(Player player, boolean rightHand) {
        if (!isVrApiAvailable() || !isPlayerInVR(player)) return null;
        
        try {
            java.lang.reflect.Method getVRPlayer = vraipInstance.getClass().getMethod("getVRPlayer", Player.class);
            Object vrPlayer = getVRPlayer.invoke(vraipInstance, player);
            
            if (vrPlayer == null) return null;
            
            String methodName = rightHand ? "getController0" : "getController1";
            java.lang.reflect.Method getCtrl = vrPlayer.getClass().getMethod(methodName);
            Object ctrlData = getCtrl.invoke(vrPlayer);
            
            if (ctrlData == null) return null;
            
            java.lang.reflect.Method getPos = ctrlData.getClass().getMethod("position");
            return (Vec3) getPos.invoke(ctrlData);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get controller direction/aim
     */
    public static Vec3 getControllerDir(Player player, boolean rightHand) {
        if (!isVrApiAvailable() || !isPlayerInVR(player)) return player.getLookAngle();
        
        try {
            java.lang.reflect.Method getVRPlayer = vraipInstance.getClass().getMethod("getVRPlayer", Player.class);
            Object vrPlayer = getVRPlayer.invoke(vraipInstance, player);
            
            if (vrPlayer == null) return player.getLookAngle();
            
            String methodName = rightHand ? "getController0" : "getController1";
            java.lang.reflect.Method getCtrl = vrPlayer.getClass().getMethod(methodName);
            Object ctrlData = getCtrl.invoke(vrPlayer);
            
            if (ctrlData == null) return player.getLookAngle();
            
            java.lang.reflect.Method getLook = ctrlData.getClass().getMethod("getLookAngle");
            return (Vec3) getLook.invoke(ctrlData);
        } catch (Exception e) {
            return player.getLookAngle();
        }
    }
    
    /**
     * Trigger haptic feedback on controller
     */
    public static void triggerHaptics(Player player, int controller, float strength, int durationMs) {
        if (!isVrApiAvailable() || !isPlayerInVR(player)) return;
        
        try {
            // Try ServerPlayer version first
            try {
                java.lang.reflect.Method m = vraipInstance.getClass().getMethod(
                    "triggerHapticPulse", int.class, float.class, 
                    net.minecraft.server.level.ServerPlayer.class
                );
                
                if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
                    m.invoke(vraipInstance, controller, strength, sp);
                }
            } catch (NoSuchMethodException e) {
                // Try version with position params
                java.lang.reflect.Method m = vraipInstance.getClass().getMethod(
                    "triggerHapticPulse", int.class, float.class, 
                    float.class, float.class, float.class,
                    net.minecraft.server.level.ServerPlayer.class
                );
                
                if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
                    Vec3 pos = player.position();
                    m.invoke(vraipInstance, controller, strength, 
                        (float)pos.x, (float)pos.y, (float)pos.z, sp);
                }
            }
        } catch (Exception e) {
            System.err.println("[LightsaberVR] Haptic fail: " + e.getMessage());
        }
    }
    
    /**
     * Check seated mode
     */
    public static boolean isSeated(Player player) {
        if (!isVrApiAvailable()) return false;
        
        try {
            java.lang.reflect.Method m = vraipInstance.getClass().getMethod("isSeated", Player.class);
            Boolean r = (Boolean) m.invoke(vraipInstance, player);
            return Boolean.TRUE.equals(r);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check left-handed mode
     */
    public static boolean isLeftHanded(Player player) {
        if (!isVrApiAvailable()) return false;
        
        try {
            java.lang.reflect.Method m = vraipInstance.getClass().getMethod("isLeftHanded", Player.class);
            Boolean r = (Boolean) m.invoke(vraipInstance, player);
            return Boolean.TRUE.equals(r);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== EVENT HANDLERS ====================
    
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Player p = (Player) event.entity;
        inVR = isPlayerInVR(p);
        
        // Apply VR reach extension to held lightsabers
        if (inVR) {
            applyVRReach(p);
        }
    }
    
    private void applyVRReach(Player p) {
        ItemStack main = p.getMainHandItem();
        ItemStack off = p.getOffhandItem();
        
        if (main.getItem() instanceof LightsaberItem li) {
            li.setVRReachExtension(2.0); // 2 extra blocks in VR
        }
        if (off.getItem() instanceof LightsaberItem li) {
            li.setVRReachExtension(2.0);
        }
    }
    
    @SubscribeEvent  
    public void onAttack(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
        if (!(event.getEntity() instanceof Player p)) return;
        
        ItemStack weapon = p.getMainHandItem();
        if (weapon.getItem() instanceof LightsaberItem) {
            triggerHaptics(p, 0, HIT_RUMBLE, 100); // Right hand hit feedback
        }
    }
    
    @SubscribeEvent
    public void onInteract(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem event) {
        Player p = (Player) event.entity;
        ItemStack item = (ItemStack) event.itemStack;
        
        if (item.getItem() instanceof LightsaberItem && inVR) {
            triggerHactics(p, 0, ACTIVATE_RUMBLE, 150); // Activation feedback
        }
    }
    
    // Fix typo in previous method
    private void triggerHactics(Player p, int c, float s, int d) { triggerHaptics(p, c, s, d); }
    
    // ==================== UTILITIES ====================
    
    /**
     * Get debug info about VR state
     */
    public static String getDebugInfo(Player p) {
        StringBuilder sb = new StringBuilder();
        sb.append("LightsaberVR Debug:\n");
        sb.append("  API Available: ").append(isVrApiAvailable()).append("\n");
        sb.append("  In VR: ").append(isPlayerInVR(p)).append("\n");
        
        if (isPlayerInVR(p)) {
            sb.append("  Seated: ").append(isSeated(p)).append("\n");
            sb.append("  Left-Handed: ").append(isLeftHanded(p)).append("\n");
            
            Vec3 rCtrl = getControllerPos(p, true);
            Vec3 lCtrl = getControllerPos(p, false);
            
            if (rCtrl != null) sb.append(String.format("  Right Ctrl: %.1f, %.1f, %.1f\n", rCtrl.x, rCtrl.y, rCtrl.z));
            if (lCtrl != null) sb.append(String.format("  Left Ctrl: %.1f, %.1f, %.1f\n", lCtrl.x, lCtrl.y, lCtrl.z));
        }
        
        return sb.toString();
    }
}

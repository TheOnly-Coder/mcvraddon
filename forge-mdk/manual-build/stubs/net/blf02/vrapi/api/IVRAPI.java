package net.blf02.vr.api;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
public interface IVRAPI {
    String getVersionString();
    int[] getVersionArray();
    boolean apiActive(Player player);
    boolean playerInVR(Player player);
    void triggerHapticPulse(int controller, float strength, ServerPlayer player);
    boolean isSeated(Player player);
    boolean isLeftHanded(Player player);
}

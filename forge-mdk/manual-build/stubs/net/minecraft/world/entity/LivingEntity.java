package net.minecraft.world.entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
public class LivingEntity extends Entity {
    public Level level;
    public void setSecondsOnFire(int seconds) {}
    public ItemStack getMainHandItem() { return null; }
    public ItemStack getOffhandItem() { return null; }
    public HumanoidArm getMainArm() { return HumanoidArm.RIGHT; }
    public float getYRot() { return 0; }
    public double getX() { return 0; }
    public double getY() { return 0; }
    public double getZ() { return 0; }
    public Vec3 position() { return Vec3.ZERO; }
    public Vec3 getLookAngle() { return Vec3.ZERO; }
    public float getEyeHeight() { return 1.62f; }
    public void startUsingItem(InteractionHand hand) {}
    public InteractionHand getUsedItemHand() { return InteractionHand.MAIN_HAND; }
    public int getUseItemRemainingTicks() { return 0; }
    public float getAttackAnim(float partialTick) { return 0; }
    public Level getLevel() { return level; }
}

package net.minecraft.world.entity.player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
public abstract class Player extends LivingEntity {
    public ItemStack getMainHandItem() { return null; }
    public ItemStack getItemInHand(InteractionHand hand) { return null; }
    public InteractionResultHolder<ItemStack> use(Level level, InteractionHand hand) { return null; }
    public void broadcastBreakEvent(InteractionHand hand) {}
}

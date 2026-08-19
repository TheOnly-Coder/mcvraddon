package net.minecraft.world.item;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
public class Item {
    public static class Properties {
        public Properties stacksTo(int i) { return this; }
        public Properties fireResistant() { return this; }
        public Properties tab(CreativeModeTab tab) { return this; }
        public Properties rarity(Rarity r) { return this; }
    }
    public Item(Properties props) {}
    public int getUseDuration(ItemStack stack) { return 0; }
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.NONE; }
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {}
    public ItemStack getDefaultInstance() { return new ItemStack(this); }
}

package net.minecraft.world.item;
public class ItemStack {
    private Item item;
    public ItemStack() {}
    public ItemStack(Item item) { this.item = item; }
    public Item getItem() { return item; }
    public boolean isEmpty() { return item == null; }
    public void shrink(int i) {}
    public void setDamageValue(int damage) {}
    public int getDamageValue() { return 0; }
    public int getMaxDamage() { return 0; }
    public boolean isDamageableItem() { return false; }
    public ItemStack copy() { return this; }
    public int getCount() { return 1; }
    public static final ItemStack EMPTY = new ItemStack();
}

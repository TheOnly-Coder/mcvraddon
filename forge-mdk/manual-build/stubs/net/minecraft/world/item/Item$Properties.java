package net.minecraft.world.item;
public class Item {
    public static class Properties {
        public Properties stacksTo(int i) { return this; }
        public Properties fireResistant() { return this; }
        public Properties tab(CreativeModeTab tab) { return this; }
        public Properties rarity(Rarity r) { return this; }
    }
}

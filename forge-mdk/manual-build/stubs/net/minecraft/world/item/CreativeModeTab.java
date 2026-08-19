package net.minecraft.world.item;
import net.minecraft.network.chat.Component;
import java.util.function.Supplier;
public class CreativeModeTab {
    public static class Builder {
        public Builder title(Component comp) { return this; }
        public Builder icon(Supplier<ItemStack> sup) { return this; }
        public CreativeModeTab build() { return null; }
    }
    public static Builder builder() { return new Builder(); }
}

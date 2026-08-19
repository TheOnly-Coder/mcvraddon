package net.minecraftforge.event;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
public class BuildCreativeModeTabContentsEvent {
    private CreativeModeTab tab;
    public CreativeModeTab getTab() { return tab; }
    public void setTab(CreativeModeTab tab) { this.tab = tab; }
    public void accept(Item item) {}
}

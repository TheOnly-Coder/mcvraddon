package net.minecraft.network.chat;
public interface Component {
    static Component translatable(String key) { return null; }
    static Component literal(String text) { return null; }
}

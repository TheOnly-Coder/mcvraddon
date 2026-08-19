package net.minecraftforge.registries;
public class RegistryObject<T> {
    public T get() { return null; }
    public boolean isPresent() { return false; }
    public static <T> RegistryObject<T> of(String name) { return null; }
    public String getId() { return ""; }
}

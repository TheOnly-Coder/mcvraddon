package net.minecraft.world;
public class InteractionResultHolder<T> {
    public static <T> InteractionResultHolder<T> success(T value) { return null; }
    public static <T> InteractionResultHolder<T> consume(T value) { return null; }
    public static <T> InteractionResultHolder<T> fail(T value) { return null; }
    public T getObject() { return null; }
}

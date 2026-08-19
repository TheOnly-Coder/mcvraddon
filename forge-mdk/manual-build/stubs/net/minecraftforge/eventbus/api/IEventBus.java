package net.minecraftforge.eventbus.api;
import java.util.function.Consumer;
public interface IEventBus {
    void addListener(Object object);
    <T> void addListener(Class<T> eventType, Object target);
    void addListener(Consumer<Object> listener);
}

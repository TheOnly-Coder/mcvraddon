package net.minecraftforge.registries;
import net.minecraftforge.eventbus.api.IEventBus;
import java.util.function.Supplier;
public class DeferredRegister<T> {
    public static <T> DeferredRegister<T> create(Registry<T> registry, String modid) { return new DeferredRegister<>(); }
    public void register(IEventBus bus) {}
    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> sup) { return null; }
}

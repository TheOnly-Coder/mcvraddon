package net.minecraft.world.level;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;
public abstract class Level {
    public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
    public boolean isClientSide = false;
    public void playSound(Object source, double x, double y, double z, Object sound, Object category, float volume, float pitch) {}
}

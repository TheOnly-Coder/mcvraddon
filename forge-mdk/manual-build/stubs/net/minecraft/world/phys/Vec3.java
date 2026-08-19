package net.minecraft.world.phys;
public class Vec3 {
    public final double x, y, z;
    public static final Vec3 ZERO = new Vec3(0, 0, 0);
    public Vec3(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    public Vec3 add(Vec3 other) { return new Vec3(x+other.x, y+other.y, z+other.z); }
    public Vec3 add(double x, double y, double z) { return new Vec3(this.x+x, this.y+y, this.z+z); }
    public Vec3 scale(double factor) { return new Vec3(x*factor, y*factor, z*factor); }
    public Vec3 normalize() { 
        double len = Math.sqrt(x*x + y*y + z*z); 
        return len == 0 ? ZERO : new Vec3(x/len, y/len, z/len); 
    }
    public double distanceTo(Vec3 other) { 
        double dx = x-other.x, dy = y-other.y, dz = z-other.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz); 
    }
}

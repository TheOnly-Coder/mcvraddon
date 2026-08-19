package net.minecraftforge.event.entity.player;
public class AttackEntityEvent {
    private Object entity;
    private Object target;
    
    public Object getEntity() { return entity; }
    public Object getTarget() { return target; }
}

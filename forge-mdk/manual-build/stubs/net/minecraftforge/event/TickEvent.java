package net.minecraftforge.event;
public class TickEvent {
    public enum Phase { START, END }
    public static class PlayerTickEvent {
        public Phase phase;
        public Object entity;
    }
}

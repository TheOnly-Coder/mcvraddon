package com.mojang.blaze3d.vertex;
public class PoseStack {
    public void pushPose() {}
    public void popPose() {}
    public void translate(double x, double y, double z) {}
    public void scale(float x, float y, float z) {}
    public void mulPose(org.joml.Matrix4f matrix) {}
    public Pose last() { return null; }
    public static class Pose {
        public org.joml.Matrix4f pose() { return null; }
    }
}

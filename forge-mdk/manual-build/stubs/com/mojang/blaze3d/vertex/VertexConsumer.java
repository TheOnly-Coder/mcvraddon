package com.mojang.blaze3d.vertex;
import org.joml.Matrix4f;
public interface VertexConsumer {
    VertexConsumer vertex(Matrix4f matrix, float x, float y, float z);
    VertexConsumer uv(float u, float v);
    VertexConsumer uv2(int uv2);
    VertexConsumer color(int r, int g, int b, int a);
    VertexConsumer normal(float x, float y, float z);
    VertexConsumer endVertex();
}

package com.theonl_coder.lightsabersvr.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.theonl_coder.lightsabersvr.item.LightsaberItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

/**
 * Custom renderer for lightsabers in VR mode.
 * Renders the blade with proper glow effects and tracks VR controllers.
 */
public class LightsaberVRRenderer {
    
    private static final float BLADE_LENGTH = 1.0F;
    private static final float BLADE_WIDTH = 0.05F;
    private static final float HANDLE_LENGTH = 0.3F;
    
    /**
     * Render a lightsaber in first-person VR view.
     * This is called by the Vivecraft rendering pipeline when in VR mode.
     */
    public static void renderLightsaberVR(PoseStack poseStack, MultiBufferSource bufferSource,
                                          ItemStack stack, HumanoidArm arm, int combinedLight) {
        if (!(stack.getItem() instanceof LightsaberItem lightsaber)) return;
        
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        
        int color = lightsaber.getBladeColor();
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        
        // Push transformation matrix
        poseStack.pushPose();
        
        // Apply arm offset
        boolean isRightHand = (arm == HumanoidArm.RIGHT);
        float armOffset = isRightHand ? -0.28F : 0.28F;
        poseStack.translate(armOffset, -0.1F, 0.4F);
        
        // Rotate based on swing progress
        float swingProgress = player.getAttackAnim(1.0F);
        if (swingProgress > 0) {
            float swingAngle = Math.sin(swingProgress * Math.PI) * 1.5F;
            poseStack.mulPose(new Matrix4f().rotationY(isRightHand ? -swingAngle : swingAngle));
        }
        
        // Render handle
        renderHandle(poseStack, bufferSource, combinedLight);
        
        // Render blade (if activated)
        if (lightsaber.isActivated()) {
            renderBlade(poseStack, bufferSource, combinedLight, r, g, b);
            
            // Add glow effect
            renderGlow(poseStack, bufferSource, r, g, b);
        }
        
        poseStack.popPose();
    }
    
    /**
     * Render the lightsaber handle/hilt
     */
    private static void renderHandle(PoseStack poseStack, MultiBufferSource bufferSource, 
                                     int combinedLight) {
        VertexConsumer consumer = bufferSource.getBuffer(
            com.mojang.blaze3d.systems.RenderType.solid());
        
        Matrix4f matrix = poseStack.last().pose();
        
        // Simple box for handle
        float hw = 0.02F; // half width
        float hl = HANDLE_LENGTH / 2; // half length
        float hh = 0.02F; // half height
        
        // Draw handle faces (simplified)
        vertex(consumer, matrix, -hw, -hh, -hl);
        vertex(consumer, matrix, hw, -hh, -hl);
        vertex(consumer, matrix, hw, hh, -hl);
        vertex(consumer, matrix, -hw, hh, -hl);
        
        vertex(consumer, matrix, -hw, -hh, hl);
        vertex(consumer, matrix, hw, -hh, hl);
        vertex(consumer, matrix, hw, hh, hl);
        vertex(consumer, matrix, -hw, hh, hl);
    }
    
    /**
     * Render the glowing blade
     */
    private static void renderBlade(PoseStack poseStack, MultiBufferSource bufferSource,
                                    int combinedLight, float r, float g, float b) {
        VertexConsumer consumer = bufferSource.getBuffer(
            com.mojang.blaze3d.systems.RenderType.energySwab(0, 0));
        
        Matrix4f matrix = poseStack.last().pose();
        
        // Move to top of handle
        poseStack.translate(0, -HANDLE_LENGTH, 0);
        matrix = poseStack.last().pose();
        
        float w = BLADE_WIDTH / 2;
        float l = BLADE_LENGTH;
        
        // Blade as elongated quad with color
        // Front face
        vertexColor(consumer, matrix, -w, -w, 0, r, g, b, 1);
        vertexColor(consumer, matrix, w, -w, 0, r, g, b, 1);
        vertexColor(consumer, matrix, w, -w, l, r, g, b, 1);
        vertexColor(consumer, matrix, -w, -w, l, r, g, b, 1);
        
        // Back face
        vertexColor(consumer, matrix, -w, w, 0, r, g, b, 1);
        vertexColor(consumer, matrix, w, w, 0, r, g, b, 1);
        vertexColor(consumer, matrix, w, w, l, r, g, b, 1);
        vertexColor(consumer, matrix, -w, w, l, r, g, b, 1);
        
        // Left face
        vertexColor(consumer, matrix, -w, -w, 0, r, g, b, 1);
        vertexColor(consumer, matrix, -w, w, 0, r, g, b, 1);
        vertexColor(consumer, matrix, -w, w, l, r, g, b, 1);
        vertexColor(consumer, matrix, -w, -w, l, r, g, b, 1);
        
        // Right face
        vertexColor(consumer, matrix, w, -w, 0, r, g, b, 1);
        vertexColor(consumer, matrix, w, w, 0, r, g, b, 1);
        vertexColor(consumer, matrix, w, w, l, r, g, b, 1);
        vertexColor(consumer, matrix, w, -w, l, r, g, b, 1);
    }
    
    /**
     * Render outer glow effect around the blade
     */
    private static void renderGlow(PoseStack poseStack, MultiBufferSource bufferSource,
                                   float r, float g, float b) {
        // Save current state
        poseStack.pushPose();
        
        // Slightly larger than blade for glow effect
        float scale = 1.5F;
        poseStack.scale(scale, scale, scale);
        
        VertexConsumer consumer = bufferSource.getBuffer(
            com.mojang.blaze3d.systems.RenderType.energySwab(0, 0));
        
        Matrix4f matrix = poseStack.last().pose();
        float alpha = 0.3F;
        
        // Glow quad (transparent version of blade)
        float w = BLADE_WIDTH / 2;
        float l = BLADE_LENGTH;
        
        vertexColor(consumer, matrix, -w*2, -w*2, 0, r, g, b, alpha);
        vertexColor(consumer, matrix, w*2, -w*2, 0, r, g, b, alpha);
        vertexColor(consumer, matrix, w*2, -w*2, l, r, g, b, alpha);
        vertexColor(consumer, matrix, -w*2, -w*2, l, r, g, b, alpha);
        
        poseStack.popPose();
    }
    
    private static void vertex(VertexConsumer consumer, Matrix4f matrix,
                               float x, float y, float z) {
        consumer.vertex(matrix, x, y, z).uv(0, 0).uv2(0).color(255, 255, 255, 255)
               .normal(0, 1, 0).endVertex();
    }
    
    private static void vertexColor(VertexConsumer consumer, Matrix4f matrix,
                                    float x, float y, float z, 
                                    float r, float g, float b, float a) {
        consumer.vertex(matrix, x, y, z).uv(0, 0).uv2(0)
               .color((int)(r*255), (int)(g*255), (int)(b*255), (int)(a*255))
               .normal(0, 1, 0).endVertex();
    }
}

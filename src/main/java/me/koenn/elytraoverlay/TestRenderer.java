package me.koenn.elytraoverlay;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, July 2017
 */
@SideOnly(Side.CLIENT)
public class TestRenderer {

    private static final int colorNeonGreen = Integer.parseInt("00FF00", 16);
    private static final ResourceLocation LINE = new ResourceLocation("elytraoverlay:textures/hud/line.png");

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (!event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();

        if (!minecraft.player.isElytraFlying()) {
            return;
        }

        ScaledResolution resolution = event.getResolution();

        int pitch = Math.round(minecraft.player.rotationPitch);
        int x = resolution.getScaledWidth() / 2 - 100;
        //minecraft.renderEngine.bindTexture(LINE);

        for (int blocks = 0; blocks < 36; ++blocks) {
            int y = blocks * 25 + (resolution.getScaledHeight() / 2 - 437);
            y = (int) ((double) y - ((double) pitch * 2.5D + 17.0D));
            int pit = blocks * 10 - 180;
            if (y > resolution.getScaledHeight() / 2 - 65 && y < resolution.getScaledHeight() / 2 + 65) {
                this.drawCenteredString(minecraft.fontRenderer, String.valueOf(pit), x, y, 1.0F, colorNeonGreen);
                Gui.drawScaledCustomSizeModalRect(x + 20, y + 4, 0, 0, 0, 0, 160, 1, 0, 0);
            }
        }

        int blocks = 0;
        while (true) {
            try {
                if (minecraft.player.posY - (double) blocks < 0.0D) {
                    blocks = (int) Math.round(minecraft.player.posY);
                    break;
                }

                Block blk = minecraft.world.getBlockState(new BlockPos(minecraft.player.posX, minecraft.player.posY - (double) blocks, minecraft.player.posZ)).getBlock();
                if (blk != Blocks.AIR) {
                    break;
                }

                ++blocks;
            } catch (Exception var6) {
                blocks = (int) Math.round(minecraft.player.posY);
                break;
            }
        }

        int scaledWidth = resolution.getScaledWidth();

        this.drawCenteredString(minecraft.fontRenderer, "Altitude: " + String.valueOf((int) Math.floor(blocks)), scaledWidth - 55, 10, 0.8F, colorNeonGreen);
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, float size, int color) {
        GL11.glScalef(size, size, size);
        float mSize = (float) Math.pow((double) size, -1.0D);
        fontRendererIn.drawString(text, (int) Math.floor((double) ((float) x / size)), (int) Math.floor((double) ((float) y / size)), color);
        GL11.glScalef(mSize, mSize, mSize);
    }
}

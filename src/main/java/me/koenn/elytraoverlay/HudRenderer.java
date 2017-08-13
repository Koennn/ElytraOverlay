package me.koenn.elytraoverlay;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, July 2017
 */
@SideOnly(Side.CLIENT)
public class HudRenderer {

    private static final int colorNeonGreen = Integer.parseInt("00FF00", 16);
    private static double speed;
    private static double vertSpeed;
    private static Vec3d prevPos;
    private static double prevHeight;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (!event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();

        /*if (!minecraft.player.isElytraFlying()) {
            return;
        }*/

        ScaledResolution resolution = event.getResolution();

        int pitch = Math.round(minecraft.player.rotationPitch);
        int x = resolution.getScaledWidth() / 2 - 180;

        for (int blocks = 0; blocks < 36; ++blocks) {
            int y = blocks * 25 + (resolution.getScaledHeight() / 2 - 437);
            y = (int) ((double) y - ((double) pitch * 2.5D + 17.0D));
            int pit = blocks * 10 - 180;
            if (y > resolution.getScaledHeight() / 2 - 80 && y < resolution.getScaledHeight() / 2 + 80) {
                this.drawCenteredString(minecraft.fontRenderer, String.valueOf(pit), x, y, 1.0F, colorNeonGreen);
                Gui.drawScaledCustomSizeModalRect(x + 20, y + 4, 0, 0, 0, 0, 20, 1, 0, 0);
            }
        }

        int lineHeight = (resolution.getScaledHeight() / 2 + 85) - (resolution.getScaledHeight() / 2 - 85);
        Gui.drawScaledCustomSizeModalRect(x + 40, resolution.getScaledHeight() / 2 - 85, 0, 0, 0, 0, 1, lineHeight, 0, 0);
        Gui.drawScaledCustomSizeModalRect(resolution.getScaledWidth() / 2 + 140, resolution.getScaledHeight() / 2 - 85, 0, 0, 0, 0, 1, lineHeight, 0, 0);

        int blocks = playerHeight(minecraft.player);

        x = resolution.getScaledWidth() / 2 + 170;
        int height = blocks - 1;

        for (blocks = 0; blocks < 45; ++blocks) {
            int y = blocks * 25 + (resolution.getScaledHeight() / 2 - 437);
            y = (int) ((double) y - ((double) height * 2.5D + 17.0D));
            int pit = blocks * 10 - 180;
            if (y > resolution.getScaledHeight() / 2 - 80 && y < resolution.getScaledHeight() / 2 + 80) {
                this.drawCenteredString(minecraft.fontRenderer, String.valueOf(pit), x, y, 1.0F, colorNeonGreen);
                Gui.drawScaledCustomSizeModalRect(x - 30, y + 4, 0, 0, 0, 0, 20, 1, 0, 0);
            }
        }

        int halfLine = lineHeight / 2;
        Gui.drawScaledCustomSizeModalRect(resolution.getScaledWidth() / 2 - 180 + 40, resolution.getScaledHeight() / 2 - 85 + halfLine, 0, 0, 0, 0, 5, 1, 0, 0);
        Gui.drawScaledCustomSizeModalRect(resolution.getScaledWidth() / 2 + 140 - 5, resolution.getScaledHeight() / 2 - 85 + halfLine, 0, 0, 0, 0, 5, 1, 0, 0);
        this.drawCenteredString(minecraft.fontRenderer, String.valueOf(Math.round(speed)), resolution.getScaledWidth() / 2 - 180 + 27, resolution.getScaledHeight() / 2 - 80 + lineHeight, 1.0F, colorNeonGreen);
        this.drawCenteredString(minecraft.fontRenderer, String.valueOf(Math.round(vertSpeed)), resolution.getScaledWidth() / 2 + 147, resolution.getScaledHeight() / 2 - 80 + lineHeight, 1.0F, colorNeonGreen);
    }

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }
        EntityPlayerSP player = minecraft.player;

        Vec3d currPos = new Vec3d(player.posX, player.posY, player.posZ);
        double currHeight = player.posY;

        if (prevPos == null) {
            prevPos = currPos;
            prevHeight = currHeight;
            return;
        }

        double distance = prevPos.distanceTo(currPos);
        double vertDistance = currHeight - prevHeight;
        speed = (distance / 0.05) * 3.6;
        vertSpeed = (vertDistance / 0.05) * 3.6;

        prevPos = currPos;
        prevHeight = currHeight;
    }

    private int playerHeight(EntityPlayerSP player) {
        Minecraft minecraft = Minecraft.getMinecraft();
        int blocks = 0;
        while (true) {
            try {
                if (player.posY - (double) blocks < 0.0D) {
                    blocks = (int) Math.round(player.posY);
                    break;
                }

                Block blk = minecraft.world.getBlockState(new BlockPos(player.posX, player.posY - (double) blocks, player.posZ)).getBlock();
                if (blk != Blocks.AIR) {
                    break;
                }

                ++blocks;
            } catch (Exception var6) {
                blocks = (int) Math.round(player.posY);
                break;
            }
        }
        return blocks;
    }

    private void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, float size, int color) {
        GL11.glScalef(size, size, size);
        float mSize = (float) Math.pow((double) size, -1.0D);
        fontRendererIn.drawString(text, (int) Math.floor((double) ((float) x / size)), (int) Math.floor((double) ((float) y / size)), color);
        GL11.glScalef(mSize, mSize, mSize);
    }
}

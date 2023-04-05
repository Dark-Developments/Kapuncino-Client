/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package coffee.client.feature.utils.Jinx;

import coffee.client.CoffeeMain;
import coffee.client.feature.utils.FakePlayerEntity;
import coffee.client.mixin.ClientConnectionInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class JinxUtils {
    public static FakePlayerEntity fakePlayer;
    public static void SpawnFakePlayer(){
        fakePlayer = new FakePlayerEntity();

        //For some reason doesnt render on first enable in world
        //So for quick fix we un-render then re-render it :> bad fix ik but easy fix
        fakePlayer.despawn();

        fakePlayer = new FakePlayerEntity();
    }

    public static void RemoveFakePlayer(){
        fakePlayer.despawn();
    }
    public static String generateMessage(int amount) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < amount; i++) message.append((char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1))));
        return message.toString();
    }

    public static String randomString(int amount) {
        StringBuilder message = new StringBuilder();
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        for (int i = 0; i < amount; i++) message.append(chars[new Random().nextInt(chars.length)]);
        return message.toString();
    }

    public static Vec3d randomPosBetween(int min, int max) {
        int x = new Random().nextInt(min, max);
        int y = 255;
        int z = new Random().nextInt(min, max);
        return new Vec3d(x, y, z);
    }

    public static Vec3d pickRandomPos() {
        int x = new Random().nextInt(16777215);
        int y = 255;
        int z = new Random().nextInt(16777215);
        return new Vec3d(x, y, z);
    }

    public static ItemStack generateItemWithNbt(String nbt, Item item) {
        try {
            ItemStack stack = new ItemStack(item);
            stack.setNbt(StringNbtReader.parse(nbt));
            return stack;
        } catch (Exception ignored) {
            return new ItemStack(item);
        }
    }

    public static Vec2f getPitchYawFromOtherEntity(Vec3d eyePos, Vec3d targetV3) {
        double vec = 57.2957763671875;
        Vec3d target = targetV3.subtract(eyePos);
        double square = Math.sqrt(target.x * target.x + target.z * target.z);
        float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(target.y, square) * vec)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(target.z, target.x) * vec) - 90.0F);
        return new Vec2f(pitch, yaw);
    }

    public static Vec3d relativeToAbsolute(Vec3d absRootPos, Vec2f rotation, Vec3d relative) {
        double xOffset = relative.x;
        double yOffset = relative.y;
        double zOffset = relative.z;
        float rot = 0.017453292F;
        float f = MathHelper.cos((rotation.y + 90.0F) * rot);
        float g = MathHelper.sin((rotation.y + 90.0F) * rot);
        float h = MathHelper.cos(-rotation.x * rot);
        float i = MathHelper.sin(-rotation.x * rot);
        float j = MathHelper.cos((-rotation.x + 90.0F) * rot);
        float k = MathHelper.sin((-rotation.x + 90.0F) * rot);
        Vec3d vec3d2 = new Vec3d(f * h, i, g * h);
        Vec3d vec3d3 = new Vec3d(f * j, k, g * j);
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0D);
        double d = vec3d2.x * zOffset + vec3d3.x * yOffset + vec3d4.x * xOffset;
        double e = vec3d2.y * zOffset + vec3d3.y * yOffset + vec3d4.y * xOffset;
        double l = vec3d2.z * zOffset + vec3d3.z * yOffset + vec3d4.z * xOffset;
        return new Vec3d(absRootPos.x + d, absRootPos.y + e, absRootPos.z + l);
    }

    public static double distance(Vec3d vec1, Vec3d vec2) {
        double dX = vec2.x - vec1.x;
        double dY = vec2.y - vec1.y;
        double dZ = vec2.z - vec1.z;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double[] directionSpeed(double speed) {
        float forward = CoffeeMain.client.player.forwardSpeed;
        float side = CoffeeMain.client.player.sidewaysSpeed;
        float yaw = CoffeeMain.client.player.prevYaw + (CoffeeMain.client.player.getYaw() - CoffeeMain.client.player.prevYaw);
        if (forward != 0.0f) {
            if (side > 0.0f) yaw += ((forward > 0.0f) ? -45 : 45);
            else if (side < 0.0f) yaw += ((forward > 0.0f) ? 45 : -45);
            side = 0.0f;
            if (forward > 0.0f) forward = 1.0f;
            else if (forward < 0.0f) forward = -1.0f;
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0F));
        double cos = Math.cos(Math.toRadians(yaw + 90.0F));
        double dx = forward * speed * cos + side * speed * sin;
        double dz = forward * speed * sin - side * speed * cos;
        return new double[]{dx, dz};
    }

    public static double roundCoordinates(double coord) {
        coord = Math.round(coord * 100.0) / 100.0;
        coord = Math.nextAfter(coord, coord + Math.signum(coord));
        if ((int) (coord * 1000.0) % 10.0 != 0.0) coord = Math.ceil(coord);
        return coord;
    }

    public static void verticalClip(double distance, ClientPlayerEntity player) {
        double distanceAbs = Math.abs(distance);
        int fakeMovePackets = (int) (distanceAbs / 10);
        if ((int)distanceAbs % 10 == 0)
            fakeMovePackets--;

        ClientConnection connection = player.networkHandler.getConnection();
        for (int i = 0; i < fakeMovePackets; i++) {
            ((ClientConnectionInvoker) connection).sendImmediately(new PlayerMoveC2SPacket.OnGroundOnly(true), null);
        }

        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            vehicle.setPosition(vehicle.getPos().add(0, distance, 0));
            ((ClientConnectionInvoker) connection).sendImmediately(new VehicleMoveC2SPacket(vehicle), null);
            return;
        }
        ((ClientConnectionInvoker) connection).sendImmediately(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX(), player.getY() + distance, player.getZ(), true), null);
        player.setPosition(player.getPos().add(0, distance, 0));
    }
}

/*
 * Copyright (c) 2022 Coffee Client, 0x150 and contributors.
 * Some rights reserved, refer to LICENSE file.
 */

package coffee.client.feature.module.impl.movement;

import coffee.client.CoffeeMain;
import coffee.client.feature.config.BooleanSetting;
import coffee.client.feature.config.DoubleSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.feature.utils.Jinx.PlayerUtils;
import coffee.client.helper.event.impl.PacketEvent;
import coffee.client.helper.util.Utils;
import coffee.client.mixinUtil.IVec3d;
import me.x150.jmessenger.MessageSubscription;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class EntityFly extends Module {

    final KeyBinding down = new KeyBinding("", GLFW.GLFW_KEY_LEFT_ALT, "");
    Entity lastRide = null;
    int kickdelay;

    final DoubleSetting verticalSpeed  = this.config.create(new DoubleSetting.Builder(10).name("vertical-speed").description("vertical-speed in blocks / second").min(1).max(100).precision(0).get());
    final DoubleSetting bypass = this.config.create(new DoubleSetting.Builder(20).name("bypass").description("ticks between anti-kick packet").min(1).max(100).precision(0).get());
    final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(10).name("speed").description("Speed in blocks / second").min(1).max(100).precision(0).get());

    public EntityFly() {
        super("EntityFly", "Allows you to fly with any entity", ModuleType.MOVEMENT);
    }

    @Override
    public void onFastTick() {
        if (client.player.hasVehicle()) {

            client.player.getVehicle().setNoGravity(true);
            client.player.getVehicle().setYaw(client.player.getYaw());

            // Horizontal movement
            Vec3d vel = PlayerUtils.getHorizontalVelocity(speed.getValue().intValue());
            double velX = vel.getX();
            double velY = 0;
            double velZ = vel.getZ();

            // Vertical movement
            if (kickdelay <= 0) return;
            if (client.options.jumpKey.isPressed()) velY += verticalSpeed.getValue().intValue() / 20;
            if (down.isPressed()) velY -= verticalSpeed.getValue().intValue() / 20;

            // Apply velocity
            ((IVec3d) client.player.getVehicle().getVelocity()).set(velX, velY, velZ);
        }
    }

    @Override
    public void tick() {
        if (client.player.hasVehicle()) {
            Entity vehicle = client.player.getVehicle();
            vehicle.setOnGround(true);
            if (kickdelay <= 0) {
                vehicle.setPosition(vehicle.getX(), vehicle.getY() - 0.04, vehicle.getZ());
                kickdelay = bypass.getValue().intValue();
            } else {
                kickdelay--;
            }
        }
    }

    @Override
    public void enable() {
        Utils.Logging.message("Press left alt to descend");
        kickdelay = bypass.getValue().intValue();
    }

    @Override
    public void disable() {
        if (lastRide != null) {
            lastRide.setNoGravity(false);
        }
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

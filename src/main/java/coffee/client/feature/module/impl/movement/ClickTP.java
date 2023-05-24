/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package coffee.client.feature.module.impl.movement;

import coffee.client.feature.config.BooleanSetting;
import coffee.client.feature.config.EnumSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.event.impl.PacketEvent;
import coffee.client.helper.render.Renderer;
import coffee.client.helper.util.Utils;
import me.x150.jmessenger.MessageSubscription;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ClickTP extends Module {


    boolean rddthreadsuspender = false;
    BlockPos originblock = new BlockPos(0,0,0);
    Vec3d movings = new Vec3d(0,0,0);

    private static BlockPos targeted = new BlockPos(0, 0, 0);
    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Normal).name("Mode").description("The way to teleport").get());
    final BooleanSetting onlyctrl = this.config.create(new BooleanSetting.Builder(false).name("Only Ctrl").description("Only teleport when the control key is pressed").get());

    public ClickTP() {
        super("ClickTP", "Teleport somewhere you clicked to", ModuleType.MOVEMENT);
        //        Events.registerEventHandlerClass(this);
    }

    private static int lengthTo(BlockPos p) {
        Vec3d v = new Vec3d(p.getX(), p.getY(), p.getZ());
        return (int) roundToN(v.distanceTo(client.player.getPos()), 0);
    }

    private static int lengthToN(BlockPos p, Vec3d origin) {
        Vec3d v = new Vec3d(p.getX(), p.getY(), p.getZ());
        return (int) roundToN(v.distanceTo(origin), 0);
    }

    public static double roundToN(double x, int n) {
        if (n == 0) return Math.floor(x);
        double factor = Math.pow(10, n);
        return Math.round(x * factor) / factor;
    }

    @Override
    public void tick() {
        BlockHitResult ray = (BlockHitResult) client.player.raycast(200, client.getTickDelta(), true);
        targeted = new BlockPos(ray.getBlockPos());
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }

    @Override
    public String getContext() {
        return null;
    }

    @MessageSubscription
    void onpacket(PacketEvent.Sent pe){
        if(pe.getPacket() instanceof PlayerMoveC2SPacket p){
            Vec3d origin = new Vec3d(p.getX(0), p.getY(0), p.getZ(0));
            if(lengthToN(originblock, origin) < 3){
                pe.cancel();
            }
        }
    }

    @MessageSubscription
    void on(coffee.client.helper.event.impl.MouseEvent event1) {
        if (event1.getButton() == 1 && event1.getType() == coffee.client.helper.event.impl.MouseEvent.Type.CLICK) {
            mousePressed();
        }
    }

    void mousePressed() {
        if (!this.isEnabled()) return;
        if (client.currentScreen != null) return;
        BlockHitResult ray = (BlockHitResult) client.player.raycast(200, client.getTickDelta(), true);
        int rd = lengthTo(ray.getBlockPos());
        int raycastdistance = rd / 7;
        BlockHitResult blockHitResult = (BlockHitResult) client.player.raycast(200, client.getTickDelta(), true);
        BlockPos d = new BlockPos(blockHitResult.getBlockPos());
        BlockPos dest = new BlockPos((int) (d.getX() + 0.5), d.getY(), (int) (d.getZ() + 0.5));
        dest = dest.offset(Direction.UP, 1);

        if (onlyctrl.getValue() && !client.options.sprintKey.isPressed()) return;

        switch (mode.getValue()) {
            case Normal -> client.player.updatePosition(dest.getX(), dest.getY(), dest.getZ());

            case Split -> {
                client.player.jump();
                ClientPlayerEntity player = client.player;
                Vec3d playerpos = player.getPos();
                double xn = dest.getX() - playerpos.x;
                double yn = dest.getY() - playerpos.y;
                double zn = dest.getZ() - playerpos.z;
                double x = xn / raycastdistance;
                double y = yn / raycastdistance;
                double z = zn / raycastdistance;
                for (int i = 0; i < raycastdistance; i++) {
                    client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX() + x, player.getY() + y, player.getZ() + z, true));
                }
                player.updatePosition(dest.getX(), dest.getY(), dest.getZ());
            }

            case Tween -> new Thread(() -> {
                int rdd = lengthTo(ray.getBlockPos());
                originblock = client.player.getBlockPos();
                BlockPos destt = new BlockPos(blockHitResult.getBlockPos());
                client.player.jump();
                ClientPlayerEntity player = client.player;
                Vec3d playerpos = player.getPos();
                double xn = destt.getX() - playerpos.x;
                double yn = destt.getY() - playerpos.y;
                double zn = destt.getZ() - playerpos.z;
                double x = xn / rdd;
                double y = yn / rdd;
                double z = zn / rdd;
                for (int i = 0; i < rdd; i++) {
                    client.player.updatePosition(player.getX() + x, player.getY() + y, player.getZ() + z);
                    try {
                        Thread.sleep(7);
                    } catch (Exception ignored) {
                    }
                    client.player.setVelocity(0, 0, 0);
                }
            }).start();

            case Experimental -> {
                new Thread(() -> {
                    originblock = client.player.getBlockPos();
                    BlockHitResult ray3 = (BlockHitResult) client.player.raycast(200, client.getTickDelta(), true);
                    int rd3 = lengthTo(ray3.getBlockPos());
                    int raycastdistance3 = rd3 / 7;
                    BlockHitResult blockHitResult3 = (BlockHitResult) client.player.raycast(200, client.getTickDelta(), true);
                    BlockPos d3 = new BlockPos(blockHitResult3.getBlockPos());
                    BlockPos dest3 = new BlockPos((int) (d3.getX() + 0.5), d3.getY(), (int) (d3.getZ() + 0.5));
                    dest3 = dest3.offset(Direction.UP, 1);
                    ClientPlayerEntity player = client.player;
                    Vec3d playerpos = player.getPos();
                    double xn = dest3.getX() - playerpos.x;
                    double yn = dest3.getY() - playerpos.y;
                    double zn = dest3.getZ() - playerpos.z;
                    double x = xn / raycastdistance3;
                    double y = yn / raycastdistance3;
                    double z = zn / raycastdistance3;
                    for (int i = 0; i < raycastdistance3; i++) {
                        client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX() + x, player.getY() + y, player.getZ() + z, true));
                        try {
                            Thread.sleep(100);
                        } catch (Exception ignored) {
                        }
                        client.player.setVelocity(0, 0, 0);
                        movings = new Vec3d(player.getX() + x, player.getY() + y, player.getZ() + z);
                    }
                }).start();
            }
        }
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        Vec3d vp = new Vec3d(targeted.getX(), targeted.getY(), targeted.getZ());
        Renderer.R3D.renderFilled(matrices, Utils.getCurrentRGB(), vp, new Vec3d(1, 1, 1));
        if(mode.getValue() == Mode.Experimental){
            Renderer.R3D.renderFilled(matrices, Utils.getCurrentRGB(), new Vec3d(originblock.getX(), originblock.getY(), originblock.getZ()), new Vec3d(1, 1, 1));
            Renderer.R3D.renderFilled(matrices, Utils.getCurrentRGB(), new Vec3d(movings.getX(), movings.getY(), movings.getZ()), new Vec3d(1, 1, 1));
        }
    }

    @Override
    public void onHudRender() {

    }

    public enum Mode {
        Split, Normal, Tween, Experimental
    }
}

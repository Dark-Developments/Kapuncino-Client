/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package coffee.client.feature.module.impl.Grief;

import java.util.Random;

import coffee.client.feature.config.DoubleSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.render.Renderer;
import coffee.client.helper.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import java.awt.Color;

public class MultiShot extends Module {

    final DoubleSetting slider = this.config.create(new DoubleSetting.Builder(1).min(1).max(20).name("Amount").description("Amount of shot").get());

    public MultiShot() {
        super("Multishot", "shoot multiple", ModuleType.GRIEF);
    }

    @Override
    public void tick() {
        if (client.options.useKey.isPressed()) {
            for (int i = 0; i < slider.getValue(); i++) {
                fire();
            }
        }
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

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (!(client.player.getMainHandStack().getItem() instanceof SpawnEggItem)) return;
        for (int ox = -10; ox < 11; ox++) {
            for (int oz = -10; oz < 11; oz++) {
                Vec3d cRot = client.player.getRotationVector();
                Vec3d a = Utils.relativeToAbsolute(client.player.getCameraPosVec(client.getTickDelta()), client.player.getRotationClient(), new Vec3d(ox, oz, 0));
                RaycastContext rc = new RaycastContext(a, a.add(cRot.multiply(200)), RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, client.player);
                BlockHitResult bhr = client.world.raycast(rc);
                Vec3d p = bhr.getPos();
                Renderer.R3D.renderFilled(matrices, Color.GRAY, p.subtract(.1, .1, .1), new Vec3d(.2, .2, .2));
            }
        }
    }

    @Override
    public void onHudRender() {

    }

    private void fire() {
        Random r = new Random();
        int ox = r.nextInt(21) - 10;
        int oz = r.nextInt(21) - 10;
        Vec3d vel = client.player.getRotationVector().normalize().multiply(3);
        Vec3d spawnPos = Utils.relativeToAbsolute(client.player.getCameraPosVec(client.getTickDelta()), client.player.getRotationClient(), new Vec3d(ox, oz, 0));
        ItemStack spawnEgg = client.player.getMainHandStack();
        if (!(spawnEgg.getItem() instanceof SpawnEggItem)) return;
        NbtCompound entityTag = spawnEgg.getOrCreateSubNbt("EntityTag");
        NbtList pos = new NbtList();
        pos.add(NbtDouble.of(spawnPos.x));
        pos.add(NbtDouble.of(spawnPos.y));
        pos.add(NbtDouble.of(spawnPos.z));
        entityTag.put("Pos", pos);
        NbtList motion = new NbtList();
        motion.add(NbtDouble.of(vel.x));
        motion.add(NbtDouble.of(vel.y));
        motion.add(NbtDouble.of(vel.z));
        entityTag.put("Motion", motion);
        entityTag.put("NoGravity", NbtByte.of(true));
        client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(client.player.getInventory().selectedSlot + 36, spawnEgg));
        client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(client.player.getPos(), Direction.UP, new BlockPos(BlockPos.ofFloored(client.player.getPos())), false), 1));
    }
}

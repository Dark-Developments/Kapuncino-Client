/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package coffee.client.feature.module.impl.Grief;

import java.util.Random;

import coffee.client.feature.config.DoubleSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import me.x150.jmessenger.MessageSubscription;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Explosion extends Module {

    final DoubleSetting size = this.config.create(new DoubleSetting.Builder(1).min(1).max(10).name("Size").description("Size of explosion").get());

    public Explosion() {
        super("Explosion", "Explode the map with cool effects", ModuleType.GRIEF);
    }

    private static String getBlockNameFromTranslationKey(String translationkey) {
        return translationkey.replace("block.minecraft.", "");
    }

    @MessageSubscription
    void on(coffee.client.helper.event.impl.MouseEvent event1) {
        if (event1.getButton() == 0 && event1.getType() == coffee.client.helper.event.impl.MouseEvent.Type.CLICK) {
            mousePressed();
        }
    }

    void mousePressed() {
        if (client.currentScreen != null) {
            return;
        }
        BlockHitResult blockHitResult = (BlockHitResult) client.player.raycast(100, client.getTickDelta(), true);
        BlockPos hit = new BlockPos(blockHitResult.getBlockPos());
        for (int x = -10; x < 10; x++)
            for (int y = -10; y < 10; y++)
                for (int z = -10; z < 10; z++) {
                    BlockPos pos = hit.add(new BlockPos(x, y, z));
                    if (new Vec3d(pos.getX(), pos.getY(), pos.getZ()).distanceTo(new Vec3d(hit.getX(), hit.getY(), hit.getZ())) > size.getValue() || client.world.getBlockState(pos).isAir())
                        continue;
                    BlockState s = client.world.getBlockState(pos);
                    client.player.networkHandler.sendChatMessage("/setblock " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " minecraft:air");
                    Double[] c = getRandoms();
                    client.player.networkHandler.sendChatMessage("/summon falling_block " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " {BlockState:{Name:\"minecraft:" + getBlockNameFromTranslationKey(s.getBlock().getTranslationKey()) + "\"},Time:1,Motion:[" + c[0] + ",1.0," + c[1] + "]}");
                }
    }

    private static Double[] getRandoms() {
        Double a = Math.random() + 0.1;
        System.out.println();
        Double b = Math.random() + 0.1;
        double c = (Math.log(a / b + 1) + 1);
        double d = (Math.log(b / a + 1) + 1);
        System.out.println(c + ":" + d + "");
        double e;
        double f;
        if (new Random().nextBoolean()) {
            e = c * 1;
        } else {
            e = c * -1;
        }
        if (new Random().nextBoolean()) {
            f = d * 1;
        } else {
            f = d * -1;
        }
        return new Double[]{e / 4, f / 4};
    }

    @Override
    public void tick() {

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

    }

    @Override
    public void onHudRender() {

    }
}

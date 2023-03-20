/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package coffee.client.feature.module.impl.Grief;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;

import java.util.Random;

public class MapFuck extends Module {

    public MapFuck() {
        super("MapFuck", "Nuke maps", ModuleType.GRIEF);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        Random r = new Random();
        ItemStack item = client.player.getMainHandStack();
        NbtList decals = new NbtList();
        for (int x = -16; x < 16; x++) {
            for (int z = -16; z < 16; z++) {
                NbtCompound decal = new NbtCompound();
                decal.putInt("x", (int) (client.player.getX() + x * 8));
                decal.putInt("z", (int) (client.player.getZ() + z * 8));
                decal.putByte("type", (byte) r.nextInt(26));
                decal.putDouble("rot", 180.0D);
                decal.putString("id", Utils.rndStr(50));
                decals.add(decal);
            }
        }
        for (int x = -16; x < 16; x++) {
            for (int z = -16; z < 16; z++) {
                NbtCompound decal = new NbtCompound();
                decal.putInt("x", (int) (client.player.getX() + x * 8) - 3);
                decal.putInt("z", (int) (client.player.getZ() + z * 8) - 3);
                decal.putByte("type", (byte) r.nextInt(26));
                decal.putDouble("rot", 180.0D);
                decal.putString("id", Utils.rndStr(50));
                decals.add(decal);
            }
        }
        for (int x = -16; x < 16; x++) {
            for (int z = -16; z < 16; z++) {
                NbtCompound decal = new NbtCompound();
                decal.putInt("x", (int) (client.player.getX() + x * 8) + 2);
                decal.putInt("z", (int) (client.player.getZ() + z * 8) + 2);
                decal.putByte("type", (byte) r.nextInt(26));
                decal.putDouble("rot", 180.0D);
                decal.putString("id", Utils.rndStr(50));
                decals.add(decal);
            }
        }
        item.getOrCreateNbt().put("Decorations", decals);
        this.setEnabled(false);
        client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, item));
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

/*
 * Copyright (c) 2023 Coffee Client, 0x150 and contributors.
 * Some rights reserved, refer to LICENSE file.
 */

package coffee.client.feature.module.impl.movement;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.event.impl.PacketEvent;
import coffee.client.mixin.network.IPlayerMoveC2SPacketMixin;
import me.x150.jmessenger.MessageSubscription;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AntiHunger extends Module {
    public AntiHunger() {
        super("AntiHunger", "Reduces hunger", ModuleType.MOVEMENT);
    }

    @MessageSubscription
    void onPacket(PacketEvent.Sent event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet){
            IPlayerMoveC2SPacketMixin accessor = (IPlayerMoveC2SPacketMixin) packet;
            accessor.setOnGround(false);
        }
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

/*
 * Copyright (c) 2022 Coffee Client, 0x150 and contributors.
 * Some rights reserved, refer to LICENSE file.
 */

package coffee.client.feature.command.impl;

import coffee.client.CoffeeMain;
import coffee.client.feature.command.Command;
import coffee.client.helper.event.impl.PacketEvent;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import me.x150.jmessenger.MessageSubscription;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class CheckCmd extends Command {
    private int checking = 0;
    private static final SimpleCommandExceptionType ALWAYS_CHECKING = new SimpleCommandExceptionType((Message) Text.of("Already executing Command Check!"));

    public CheckCmd() {
        super("CheckCmd", "Check if command blocks are enabled", "checkCmd");
    }

    @Override
    public void onExecute(String[] args) {
        if (this.checking > 0) try {
            throw ALWAYS_CHECKING.create();
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        this.checking = 200;
        client.player.networkHandler.sendPacket((Packet<?>) new UpdateCommandBlockC2SPacket(client.player.getBlockPos(), "", CommandBlockBlockEntity.Type.AUTO, false, false, false));
        message("Checking..");
        this.checking = 0;
    }

    @MessageSubscription
    void onpacket(PacketEvent.Received event) {
        if (!(event.getPacket() instanceof GameMessageS2CPacket))
            return;
        Text message = ((GameMessageS2CPacket) event.getPacket()).content();
        if (message.getContent() instanceof TranslatableTextContent) {
            String key = ((TranslatableTextContent) message.getContent()).getKey();
            if (key.equals("advMode.notEnabled")) {
                message("Command blocks are deactivated");
                event.cancel();
                this.checking = 0;
            } else if (key.equals("advMode.notAllowed") || key.equals("advMode.setCommand.success")) {
                message("Command blocks are activated");
                event.cancel();
                this.checking = 0;
            }
        }
    }
}

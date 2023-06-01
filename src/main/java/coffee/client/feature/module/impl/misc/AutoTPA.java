package coffee.client.feature.module.impl.misc;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.event.impl.PacketEvent;
import me.x150.jmessenger.MessageSubscription;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

import java.text.Format;

public class AutoTPA extends Module {
    public AutoTPA() {
        super("AutoTPA", "Auto accept TPA requests", ModuleType.MISC);
    }

    @MessageSubscription
    void onpacket(PacketEvent.Received event){
        if (event.getPacket() instanceof ChatMessageS2CPacket packet){
            String message = packet.toString();
            if (message.toLowerCase().contains("has requested to teleport to you") || message.toLowerCase().contains("has requested that you teleport to them")){
                client.player.networkHandler.sendChatCommand("tpaccept");
            }
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

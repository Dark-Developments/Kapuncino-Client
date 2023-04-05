package coffee.client.feature.command.impl;

import coffee.client.feature.command.Command;
import coffee.client.feature.command.exception.CommandException;
import coffee.client.helper.util.Utils;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class ahdupe extends Command {
    public ahdupe() {
        super("Ahdupe", "Auction house dupe", "ahdupe");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (client.player.getMainHandStack() != null){
            client.player.networkHandler.sendCommand("ah sell 1000000");
            client.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal("kekw")));
        }
        else Utils.Logging.error("Hold item you want to dupe");
    }
}

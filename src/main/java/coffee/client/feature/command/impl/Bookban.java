package coffee.client.feature.command.impl;

import coffee.client.feature.command.Command;
import coffee.client.feature.command.exception.CommandException;
import coffee.client.helper.util.Utils;
import coffee.client.mixin.Accessor.TextHandlerAccessor;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.text.Style;

import java.util.*;

public class Bookban extends Command {
    public Bookban() {
        super("bookban", "Makes a bookban book", "Bookban");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (client.player.getMainHandStack().getItem() != Items.WRITABLE_BOOK) Utils.Logging.message("You must be holding a book!");
        writeBook();
        Utils.Logging.message("Successfully wrote book!");
    }

    private void writeBook() {
        String booktext = "ⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫⅫ";
        Optional<String> title = Optional.of("$$$$$$$$$$$$$$$");

        String[] pages = new String[100];
        NbtList pagesNbt = new NbtList();

        for(int i = 0; i < 100; i++) {
            pages[i] = booktext;
            pagesNbt.add(NbtString.of(pages[i]));
        }

        // Write data to book
        client.player.networkHandler.sendPacket(new BookUpdateC2SPacket(client.player.getInventory().selectedSlot, List.of(pages), title));
    }
}

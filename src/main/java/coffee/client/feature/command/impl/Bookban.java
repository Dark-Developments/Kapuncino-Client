package coffee.client.feature.command.impl;

import coffee.client.feature.command.Command;
import coffee.client.feature.command.coloring.ArgumentType;
import coffee.client.feature.command.coloring.PossibleArgument;
import coffee.client.feature.command.coloring.StaticArgumentServer;
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
    private Random random;
    private String btitle;
    public Bookban() {
        super("bookban", "Makes a bookban book", "Bookban");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return new PossibleArgument(ArgumentType.STRING, "<message>");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide book title");
        btitle = args[0].toLowerCase();
        if (btitle.isEmpty()) btitle = "§b§lHoly Bible";
        if (client.player.getMainHandStack().getItem() != Items.WRITABLE_BOOK) Utils.Logging.message("You must be holding a book!");
        int origin = 0x0800;
        int bound = 0x10FFFF;
        random = new Random();

        writeBook(
                // Generate a random load of ints to use as random characters
                random.ints(origin, bound)
                        .filter(i -> !Character.isWhitespace(i) && i != '\r' && i != '\n')
                        .iterator()
        );
        Utils.Logging.message("Successfully wrote book!");
    }

    private void writeBook(PrimitiveIterator.OfInt chars) {
        ArrayList<String> pages = new ArrayList<>();

        for (int pageI = 0; pageI < 100; pageI++) {
            // Check if the stream is empty before creating a new page
            if (!chars.hasNext()) break;

            StringBuilder page = new StringBuilder();

            for (int lineI = 0; lineI < 13; lineI++) {
                // Check if the stream is empty before creating a new line
                if (!chars.hasNext()) break;

                double lineWidth = 0;
                StringBuilder line = new StringBuilder();

                while (true) {
                    // Check if the stream is empty
                    if (!chars.hasNext()) break;

                    // Get the next character
                    int nextChar = chars.nextInt();

                    // Ignore newline chars when writing lines, should already be organised
                    if (nextChar == '\r' || nextChar == '\n') break;

                    // Make sure the character will fit on the line
                    double charWidth = ((TextHandlerAccessor) client.textRenderer.getTextHandler()).getWidthRetriever().getWidth(nextChar, Style.EMPTY);
                    if (lineWidth + charWidth > 114) break;

                    // Append it to the line
                    line.appendCodePoint(nextChar);
                    lineWidth += charWidth;
                }

                // Append the line to the page
                page.append(line).append('\n');
            }

            // Append page to the page list
            pages.add(page.toString());
        }

        // Write data to book
        client.player.getMainHandStack().setSubNbt("title", NbtString.of(btitle));
        client.player.getMainHandStack().setSubNbt("author", NbtString.of(client.player.getGameProfile().getName()));

        // Write pages NBT
        NbtList pageNbt = new NbtList();
        pages.stream().map(NbtString::of).forEach(pageNbt::add);
        if (!pages.isEmpty()) client.player.getMainHandStack().setSubNbt("pages", pageNbt);

        // Send book update to server
        client.player.networkHandler.sendPacket(new BookUpdateC2SPacket(client.player.getInventory().selectedSlot, pages, Optional.of(btitle)));
    }
}

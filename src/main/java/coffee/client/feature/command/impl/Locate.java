package coffee.client.feature.command.impl;

import coffee.client.feature.command.Command;
import coffee.client.feature.command.coloring.ArgumentType;
import coffee.client.feature.command.coloring.PossibleArgument;
import coffee.client.feature.command.coloring.StaticArgumentServer;
import coffee.client.feature.command.exception.CommandException;
import coffee.client.helper.render.Renderer;
import coffee.client.helper.util.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class Locate extends Command {
    public Locate() {
        super("Locate", "Locate Things", "locate");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index,
                new PossibleArgument(ArgumentType.STRING, "buried-treasure"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide source");
        String arg = args[0];

        switch (arg){
            case "buried-treasure" -> {
                ItemStack item = client.player.getMainHandStack();
                if (item.getItem() == Items.FILLED_MAP){
                    NbtList nbtList = (NbtList) item.getNbt().get("Decorations");
                    if (nbtList == null){
                        Utils.Logging.error("Couldnt locate");
                        return;
                    }
                    NbtCompound nbt = nbtList.getCompound(0);
                    if (nbt == null){
                        Utils.Logging.error("Couldnt locate");
                        return;
                    }
                    Vec3d coords = new Vec3d(nbt.getDouble("x"),nbt.getDouble("y"),nbt.getDouble("z"));
                    MutableText text = (MutableText) Text.of("Buried Treasure at: ");
                    text.append(coords.getX() + " " + coords.getY() + " " + coords.getZ());

                    Utils.Logging.message(text);
                }
                else Utils.Logging.error("Hold a treasure map");
            }
        }
    }
}

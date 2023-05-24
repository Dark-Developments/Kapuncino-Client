package coffee.client.feature.command.impl;

import coffee.client.CoffeeMain;
import coffee.client.feature.command.Command;
import coffee.client.feature.command.argument.PlayerFromNameArgumentParser;
import coffee.client.feature.command.coloring.ArgumentType;
import coffee.client.feature.command.coloring.PossibleArgument;
import coffee.client.feature.command.coloring.StaticArgumentServer;
import coffee.client.feature.command.exception.CommandException;
import coffee.client.helper.util.Utils;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;

public class FindCoords extends Command {
    public FindCoords() {
        super("Findcoords", "Get the coords of a rendered player", "findcoords");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index,
                new PossibleArgument(ArgumentType.STRING,
                        Objects.requireNonNull(CoffeeMain.client.world)
                                .getPlayers()
                                .stream()
                                .map(abstractClientPlayerEntity -> abstractClientPlayerEntity.getGameProfile().getName())
                                .toList()
                                .toArray(String[]::new)));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide target username");
        PlayerEntity t = new PlayerFromNameArgumentParser(true).parse(args[0]);

        Utils.Logging.message(t.getEntityName() + " Coords: " + t.getBlockX() + " "+ t.getBlockY() + " " + t.getBlockZ());
        client.keyboard.setClipboard(t.getBlockX() + " "+ t.getBlockY() + " " + t.getBlockZ());
    }
}

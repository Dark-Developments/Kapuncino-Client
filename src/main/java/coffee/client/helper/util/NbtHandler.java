package coffee.client.helper.util;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import static coffee.client.helper.util.StrongholdPosition.*;


public class NbtHandler {

    public static Entity enderEye;

    public static double[] read() {
        ThrowDataInterface throwData = (ThrowDataInterface)MinecraftClient.getInstance().player;
        NbtList nbtList = throwData.getThrowData().getList("strongholdfinder.previousThrowData", NbtType.DOUBLE);
        return new double[]{nbtList.getDouble(0), nbtList.getDouble(1), nbtList.getDouble(2), nbtList.getDouble(3)};
    }

    public static void write(double throwX, double throwZ, double eyeX, double eyeZ) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        ThrowDataInterface throwData = (ThrowDataInterface)MinecraftClient.getInstance().player;
        if (throwData.getThrowData().contains("strongholdfinder.previousThrowData")) {
            handle(throwX, throwZ, eyeX, eyeZ);
            throwData.getThrowData().remove("strongholdfinder.previousThrowData");
            enderEye = null;
            return;
        }

        player.sendMessage(Text.of(("triangulate.stronghold.throw1")));

        var data = new NbtList();

        data.add(NbtDouble.of(throwX));
        data.add(NbtDouble.of(throwZ));
        data.add(NbtDouble.of(eyeX));
        data.add(NbtDouble.of(eyeZ));

        throwData.getThrowData().put("strongholdfinder.previousThrowData", data);
    }

    public static void handle(double throwX, double throwZ, double eyeX, double eyeZ) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        double[] previousThrow = read();
        double[] currentThrow = {throwX, throwZ, eyeX, eyeZ};

        int[] strongholdPosition = triangulatePosition(previousThrow, currentThrow);
        int x = strongholdPosition[0];
        int z = strongholdPosition[1];
        int distance = getDistance(player.getBlockPos(), strongholdPosition);
        MutableText text = Texts.bracketed(Text.of("chat.coordinates" + x + "~" + z));
        player.sendMessage(Text.of("triangulate.stronghold.throw2" + text + distance));
    }

    public static int value = -1;

    public static void tick(MinecraftClient client) {
        if (value < 0) {
            return;
        }

        value--;

        if (value != 0) {
            return;
        }

        double playerX = client.player.getX();
        double playerZ = client.player.getZ();
        double eyeX = enderEye.getX();
        double eyeZ = enderEye.getZ();
        write(playerX, playerZ, eyeX, eyeZ);
    }

    public static void setDelay(int delay) {
        value = delay;
    }
}

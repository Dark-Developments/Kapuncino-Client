package coffee.client.feature.module.impl.movement;

import coffee.client.CoffeeMain;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;

public class GhostBlockFly extends Module {
    public GhostBlockFly() {
        super("GhostBlockFly", "Fly using ghost blocks", ModuleType.MOVEMENT);
    }

    BlockState state = null;
    BlockPos pos = null;

    @Override
    public void tick() {
        assert CoffeeMain.client.world != null;
        assert CoffeeMain.client.player != null;
        if (CoffeeMain.client.player.getBlockPos().add(0, -1, 0) != pos && pos != null && state != null) {
            CoffeeMain.client.world.setBlockState(pos, state);
        }

        pos = CoffeeMain.client.player.getBlockPos().add(0, -1, 0);
        state = CoffeeMain.client.world.getBlockState(pos);

        if (!CoffeeMain.client.options.sneakKey.isPressed() && CoffeeMain.client.world.getBlockState(pos).getBlock() instanceof AirBlock && pos != null) {
            CoffeeMain.client.world.setBlockState(pos, Blocks.BARRIER.getDefaultState());
        }

        if (CoffeeMain.client.options.sneakKey.isPressed() && CoffeeMain.client.world.getBlockState(pos).getBlock() instanceof AirBlock && pos != null) {
            CoffeeMain.client.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        assert CoffeeMain.client.world != null;
        CoffeeMain.client.world.setBlockState(pos, state);
        pos = null;
        BlockState state = null;
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

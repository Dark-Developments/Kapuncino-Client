package coffee.client.feature.module.impl.world;

import coffee.client.CoffeeMain;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.util.Utils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class GhostHand extends Module {
    private final Set<BlockPos> posList = new ObjectOpenHashSet<>();
    public GhostHand() {
        super("GhostHand", "Open containers through walls", ModuleType.WORLD);
    }

    @Override
    public void tick() {
        if (!(CoffeeMain.client.options.useKey.isPressed())) return;

        for (BlockEntity blockEntity : Utils.blockEntities()) {
            if (BlockPos.ofFloored(CoffeeMain.client.player.raycast(CoffeeMain.client.interactionManager.getReachDistance(), CoffeeMain.client.getTickDelta(), false).getPos()).equals(blockEntity.getPos())) return;
        }

        Vec3d direction = new Vec3d(0, 0, 0.1)
                .rotateX(-(float) Math.toRadians(CoffeeMain.client.player.getPitch()))
                .rotateY(-(float) Math.toRadians(CoffeeMain.client.player.getYaw()));

        posList.clear();

        for (int i = 1; i < CoffeeMain.client.interactionManager.getReachDistance() * 10; i++) {
            BlockPos pos = BlockPos.ofFloored(CoffeeMain.client.player.getCameraPosVec(CoffeeMain.client.getTickDelta()).add(direction.multiply(i)));

            if (posList.contains(pos)) continue;
            posList.add(pos);

            for (BlockEntity blockEntity : Utils.blockEntities()) {
                if (blockEntity.getPos().equals(pos)) {
                    CoffeeMain.client.interactionManager.interactBlock(CoffeeMain.client.player, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), Direction.UP, pos, true));
                    return;
                }
            }
        }
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

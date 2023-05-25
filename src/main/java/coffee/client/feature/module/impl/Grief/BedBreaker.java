package coffee.client.feature.module.impl.Grief;

import coffee.client.CoffeeMain;
import coffee.client.feature.config.BooleanSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import net.minecraft.block.BedBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class BedBreaker extends Module {
    final BooleanSetting armswing = this.config.create(new BooleanSetting.Builder(true).name("Armswing")
            .description("Send armswing packet")
            .get());

    final BooleanSetting rotate = this.config.create(new BooleanSetting.Builder(true).name("Rotate")
            .description("Force face block")
            .get());
    public BedBreaker() {
        super("BedBreaker", "Automatically break nearby beds", ModuleType.GRIEF);
    }

    @Override
    public void tick() {
        for(int x = -3; x < 3; x++) {
            for(int y = -3; y < 3; y++) {
                for(int z = -3; z < 3; z++) {
                    BlockPos pos = new BlockPos((int) (client.player.getX() + x), (int) (client.player.getY() + y), (int) (client.player.getZ() + z));

                    if (client.world.getBlockState(pos).getBlock() instanceof BedBlock) {
                        if (armswing.getValue()) client.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                        if (rotate.getValue()) client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) getYaw(pos), (float) getPitch(pos), client.player.isOnGround()));


                        CoffeeMain.client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.DOWN));
                    }
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

    private static double getYaw(BlockPos pos) {
        return client.player.getYaw() + MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(pos.getZ() + 0.5 - client.player.getZ(), pos.getX() + 0.5 - client.player.getX())) - 90f - client.player.getYaw());
    }

    private static double getPitch(BlockPos pos) {
        double diffX = pos.getX() + 0.5 - client.player.getX();
        double diffY = pos.getY() + 0.5 - (client.player.getY() + client.player.getEyeHeight(client.player.getPose()));
        double diffZ = pos.getZ() + 0.5 - client.player.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return client.player.getPitch() + MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) - client.player.getPitch());
    }
}

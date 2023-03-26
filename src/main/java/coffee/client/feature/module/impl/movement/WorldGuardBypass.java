package coffee.client.feature.module.impl.movement;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class WorldGuardBypass extends Module {
    public WorldGuardBypass() {
        super("WGMoveBypass", "Bypasses WorldGuard Enter deny", ModuleType.MOVEMENT);
    }

    @Override
    public void onFastTick() {
        double hspeed = 0.0625D;
        double vspeed = 0.0625D;

        Vec3d forward = new Vec3d(0, 0, hspeed).rotateY(-(float) Math.toRadians(Math.round(client.player.getYaw() / 90) * 90));
        Vec3d moveVec = Vec3d.ZERO;


        if (client.options.forwardKey.isPressed()) {
            moveVec = moveVec.add(forward);
            client.player.setVelocity(0, 0, 0);
        } else if (client.options.backKey.isPressed()) {
            moveVec = moveVec.add(forward.negate());
            client.player.setVelocity(0, 0, 0);
        }

        else if (client.options.leftKey.isPressed()) {
            client.player.setVelocity(0, 0, 0);
            moveVec = moveVec.add(forward.rotateY((float) Math.toRadians(90)));
        }else if (client.options.rightKey.isPressed()) {
            moveVec = moveVec.add(forward.rotateY((float) -Math.toRadians(90)));
            client.player.setVelocity(0, 0, 0);
        }

        else if (client.options.jumpKey.isPressed()) {
            moveVec = moveVec.add(0, vspeed, 0);
            client.player.setVelocity(0, 0, 0);
        }else if (client.options.sneakKey.isPressed()) {
            moveVec = moveVec.add(0, -vspeed, 0);
            client.player.setVelocity(0, 0, 0);
        }

        client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                client.player.getX() + moveVec.x, client.player.getY() + moveVec.y, client.player.getZ() + moveVec.z, false));

        client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                client.player.getX() + moveVec.x, client.player.getY() -100, client.player.getZ() + moveVec.z, true));
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

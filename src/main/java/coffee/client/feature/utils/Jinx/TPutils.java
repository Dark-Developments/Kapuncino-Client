/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package coffee.client.feature.utils.Jinx;

import baritone.pathing.calc.AStarPathFinder;
import coffee.client.mixin.ClientConnectionInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class TPutils {

    public static void teleportFromTo(MinecraftClient mc, Vec3d fromPos, Vec3d toPos, double delta){
        ClientConnection connection = mc.player.networkHandler.getConnection();
        double distancePerBlock = delta;
        double targetDistance = Math.ceil(fromPos.distanceTo(toPos) / distancePerBlock);
        for (int i =1; i<=targetDistance; i++){
            Vec3d tempPos = fromPos.lerp(toPos, i / targetDistance);
            ((ClientConnectionInvoker) connection).sendImmediately(new PlayerMoveC2SPacket.PositionAndOnGround(tempPos.x, tempPos.y, tempPos.z, true), null);

            if (i%4 == 0){
                try{
                    Thread.sleep((long)((1/20) * 1000));
                }
                catch (InterruptedException e){

                }
            }

        }
    }
}

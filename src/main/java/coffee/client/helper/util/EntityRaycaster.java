package coffee.client.helper.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class EntityRaycaster {

    private static Predicate<Entity> predicate = entity -> !entity.isSpectator();

    public static EntityHitResult raycast(PlayerEntity player) {
        double reach = 60;
        Vec3d position = player.getCameraPosVec(0);
        Vec3d viewVec = player.getRotationVec(0F);
        Vec3d stoppingPoint = position.add(viewVec.x * reach, viewVec.y * reach, viewVec.z * reach);
        Box searchBox = player.getBoundingBox().stretch(viewVec.multiply(reach)).expand(1D, 1D, 1D);
        return ProjectileUtil.raycast(player, position, stoppingPoint, searchBox, predicate, reach*reach);
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

        EntityHitResult hitResult = raycast(client.player.getInventory().player);

        if (hitResult != null && hitResult.getEntity() != null) {
            NbtHandler.enderEye = hitResult.getEntity();
            NbtHandler.setDelay(6);
        }

    }

    public static void setDelay(int delay) {
        value = delay;
    }
}

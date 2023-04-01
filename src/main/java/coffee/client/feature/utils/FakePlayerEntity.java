package coffee.client.feature.utils;

import coffee.client.CoffeeMain;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;

public class FakePlayerEntity extends OtherClientPlayerEntity
{

    private final ClientPlayerEntity player = CoffeeMain.client.player;
    private final ClientWorld world = CoffeeMain.client.world;

    public FakePlayerEntity()
    {
        super(CoffeeMain.client.world, CoffeeMain.client.player.getGameProfile());

        //get inv
        getInventory().clone(player.getInventory());


        //get rotations
        headYaw = player.headYaw;
        bodyYaw = player.bodyYaw;

        spawn();
    }

    private void spawn()
    {
        world.addEntity(getId(), this);
    }

    public void despawn()
    {
        player.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(), getPitch());
        discard();
    }
}

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

    //Taken from wurst client <333 (I actually love you sm wurst client omg)
    private final ClientPlayerEntity player = CoffeeMain.client.player;
    private final ClientWorld world = CoffeeMain.client.world;

    public FakePlayerEntity()
    {
        super(CoffeeMain.client.world, CoffeeMain.client.player.getGameProfile());
        copyPositionAndRotation(player);

        copyInventory();
        copyPlayerModel(player, this);
        copyRotation();
        resetCapeMovement();

        spawn();
    }

    private void copyInventory()
    {
        getInventory().clone(player.getInventory());
    }

    private void copyPlayerModel(Entity from, Entity to)
    {
        DataTracker fromTracker = from.getDataTracker();
        DataTracker toTracker = to.getDataTracker();
        Byte playerModel = fromTracker.get(PlayerEntity.PLAYER_MODEL_PARTS);
        toTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
    }

    private void copyRotation()
    {
        headYaw = player.headYaw;
        bodyYaw = player.bodyYaw;
    }

    private void resetCapeMovement()
    {
        capeX = getX();
        capeY = getY();
        capeZ = getZ();
    }

    private void spawn()
    {
        world.addEntity(getId(), this);
    }

    public void despawn()
    {
        discard();
    }

    public void resetPlayerPosition()
    {
        player.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(),
                getPitch());
    }
}

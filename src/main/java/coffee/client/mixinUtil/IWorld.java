package coffee.client.mixinUtil;

import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

public interface IWorld
{
    public Stream<VoxelShape> getBlockCollisionsStream(@Nullable Entity entity,
                                                       Box box);

    public default Stream<Box> getCollidingBoxes(@Nullable Entity entity,
                                                 Box box)
    {
        return getBlockCollisionsStream(entity, box)
                .flatMap(vs -> vs.getBoundingBoxes().stream())
                .filter(b -> b.intersects(box));
    }
}

package coffee.client.helper.util.World;

import coffee.client.CoffeeMain;
import coffee.client.mixin.Accessor.ClientChunkManagerAccessor;
import coffee.client.mixin.Accessor.ClientChunkMapAccessor;
import net.minecraft.world.chunk.Chunk;

import java.util.Iterator;

public class ChunkIterator implements Iterator<Chunk> {
    private final ClientChunkMapAccessor map = (ClientChunkMapAccessor) (Object) ((ClientChunkManagerAccessor) CoffeeMain.client.world.getChunkManager()).getChunks();
    private final boolean onlyWithLoadedNeighbours;

    private int i = 0;
    private Chunk chunk;

    public ChunkIterator(boolean onlyWithLoadedNeighbours) {
        this.onlyWithLoadedNeighbours = onlyWithLoadedNeighbours;

        getNext();
    }

    private Chunk getNext() {
        Chunk prev = chunk;
        chunk = null;

        while (i < map.getChunks().length()) {
            chunk = map.getChunks().get(i++);
            if (chunk != null && (!onlyWithLoadedNeighbours || isInRadius(chunk))) break;
        }

        return prev;
    }

    private boolean isInRadius(Chunk chunk) {
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;

        return CoffeeMain.client.world.getChunkManager().isChunkLoaded(x + 1, z) && CoffeeMain.client.world.getChunkManager().isChunkLoaded(x - 1, z) && CoffeeMain.client.world.getChunkManager().isChunkLoaded(x, z + 1) && CoffeeMain.client.world.getChunkManager().isChunkLoaded(x, z - 1);
    }

    @Override
    public boolean hasNext() {
        return chunk != null;
    }

    @Override
    public Chunk next() {
        return getNext();
    }
}

package coffee.client.mixin;

import coffee.client.helper.util.ThrowDataInterface;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class ThrowDataRetriever implements ThrowDataInterface {
    private NbtCompound throwData;

    @Override
    public NbtCompound getThrowData() {
        if(this.throwData == null) {
            this.throwData = new NbtCompound();
        }
        return throwData;
    }
}

package coffee.client.mixin;

import coffee.client.feature.module.ModuleRegistry;
import coffee.client.helper.util.EntityRaycaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;
//test


@Mixin(EnderEyeItem.class)
public abstract class EyeThrownMixin {
    private static Predicate<Entity> predicate = entity -> !entity.isSpectator();

    @Inject(method = "use", at = @At("TAIL"))
    protected void afterUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable info) {
//        if (ModuleRegistry.getByClass(StrongholdLocator.class).isEnabled()) EntityRaycaster.setDelay(4);
    }
}

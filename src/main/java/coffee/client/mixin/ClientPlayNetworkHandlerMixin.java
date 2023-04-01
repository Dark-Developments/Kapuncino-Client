package coffee.client.mixin;

import coffee.client.CoffeeMain;
import coffee.client.helper.meteorEvent.game.SendMessageEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Unique
    private boolean ignoreChatMessage;
    @Shadow
    public abstract void sendChatMessage(String content);

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if (ignoreChatMessage) return;
        SendMessageEvent event = CoffeeMain.EVENT_BUS.post(SendMessageEvent.get(message));

        if (!event.isCancelled()) {
            ignoreChatMessage = true;
            sendChatMessage(event.message);
            ignoreChatMessage = false;
        }
        ci.cancel();
        return;
    }
}

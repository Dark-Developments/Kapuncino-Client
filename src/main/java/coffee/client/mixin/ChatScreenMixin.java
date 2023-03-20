package coffee.client.mixin;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleRegistry;
import coffee.client.feature.module.impl.exploit.NoChatNormalisation;
import coffee.client.feature.module.impl.render.NoMessageIndicators;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @Shadow protected TextFieldWidget chatField;

    @Redirect(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;normalize(Ljava/lang/String;)Ljava/lang/String;"))
    String onChatMessageNormalize(ChatScreen instance, String chatText) {
        if (!chatText.isEmpty()) MinecraftClient.getInstance().inGameHud.getChatHud()
                .addToMessageHistory(chatText);

        String text = chatText;
        Module nochatnormalisation = ModuleRegistry.getByClass(NoChatNormalisation.class);
        if (!nochatnormalisation.isEnabled()) text = instance.normalize(chatText);
        return text;
    }
}

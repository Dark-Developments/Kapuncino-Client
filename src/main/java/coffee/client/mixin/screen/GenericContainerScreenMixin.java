package coffee.client.mixin.screen;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleRegistry;
import coffee.client.feature.module.impl.Grief.StorageVoider;
import coffee.client.feature.module.impl.exploit.ChatFilterBypass;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GenericContainerScreen.class)
public abstract class GenericContainerScreenMixin extends HandledScreen<GenericContainerScreenHandler> implements ScreenHandlerProvider<GenericContainerScreenHandler> {
    public GenericContainerScreenMixin(GenericContainerScreenHandler container, PlayerInventory playerInventory, Text name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void init() {
        super.init();
        StorageVoider storageVoider = ModuleRegistry.getByClass(StorageVoider.class);

        if (storageVoider.isEnabled()) {
            addDrawableChild(
                    new ButtonWidget.Builder(Text.literal("Voider"), button -> storageVoider.voider(handler))
                            .position(x + backgroundWidth - 46, y + 2)
                            .size(56, 14)
                            .build()
            );
        }
    }
}

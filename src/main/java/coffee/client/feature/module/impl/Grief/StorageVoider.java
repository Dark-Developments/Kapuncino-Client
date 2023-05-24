package coffee.client.feature.module.impl.Grief;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class StorageVoider extends Module {
    public StorageVoider() {
        super("StorageVoider", "Void items in a storage block", ModuleType.GRIEF);
    }

    private int getRows(ScreenHandler handler) {
        return (handler instanceof GenericContainerScreenHandler ? ((GenericContainerScreenHandler) handler).getRows() : 3);
    }
    private void moveSlots(ScreenHandler handler, int start, int end) {
        for (int i = start; i < end; i++) {
            if (!handler.getSlot(i).hasStack()) continue;

            // Exit if user closes screen
            if (client.currentScreen == null) break;
            client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, i, 50, SlotActionType.SWAP, client.player);
        }
    }

    public void voider(ScreenHandler handler) {
        moveSlots(handler, 0, getRows(handler) * 9);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

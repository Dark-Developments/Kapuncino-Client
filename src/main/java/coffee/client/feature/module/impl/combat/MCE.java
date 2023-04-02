package coffee.client.feature.module.impl.combat;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.feature.utils.InvUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public class MCE extends Module {
    int pealcount;
    public MCE() {
        super("MCE", "Throw enderpearl on middle-click", ModuleType.COMBAT);
    }

    @Override
    public void onFastTick() {
        if (client.options.pickItemKey.isPressed()){
            if(client.currentScreen instanceof GenericContainerScreen) return;
            pealcount = InvUtils.getamount(Items.ENDER_PEARL);

            if (pealcount == 0) return;
            int index = InvUtils.finditem(Items.ENDER_PEARL);
            if(index == -1) return;

            putinmain(index);
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            putback(index);
        }
    }

    private void putinmain(int slot){
        client.interactionManager.clickSlot(0, InvUtils.getslot(slot), 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(0, 36, 0, SlotActionType.PICKUP, client.player);
    }

    private void putback(int slot){
        client.interactionManager.clickSlot(0, 36, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(0, InvUtils.getslot(slot), 0, SlotActionType.PICKUP, client.player);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        pealcount = 0;
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

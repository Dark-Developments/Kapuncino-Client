package coffee.client.feature.module.impl.combat;

import coffee.client.feature.config.BooleanSetting;
import coffee.client.feature.config.DoubleSetting;
import coffee.client.feature.config.EnumSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.feature.module.impl.movement.Flight;
import coffee.client.helper.util.InventoryUtils;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    private int totemCount = 0;

    public AutoTotem() {
        super("AutoTotem", "Automatically put totems in offhand", ModuleType.COMBAT);
    }

    final EnumSetting<AutoTotem.modes> mode = this.config.create(new EnumSetting.Builder<>(modes.Strict).name("Mode").description("Mode to use").get());

    final DoubleSetting health = this.config.create(new DoubleSetting.Builder(3).name("health").description("Health to activate at (while using smart) per half-heart").min(1).max(18).precision(0).get());

    @Override
    public void tick() {
        if(client.currentScreen instanceof GenericContainerScreen) return;
        totemCount = InventoryUtils.amountInInventory(Items.TOTEM_OF_UNDYING);

        if (client.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;
        if (totemCount == 0) return;
        int index = InventoryUtils.findItem(Items.TOTEM_OF_UNDYING);
        if(index == -1) return;

        switch (mode.getValue()){
            case Smart -> {
                if (client.player.getHealth() <= health.getValue()){
                    putinoffhand(index);
                }
            }

            case Strict -> {
                putinoffhand(index);
            }
        }

    }

    private void putinoffhand(int slot){
        client.interactionManager.clickSlot(0, InventoryUtils.getSlotIndex(slot), 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(0, InventoryUtils.getSlotIndex(slot), 0, SlotActionType.PICKUP, client.player);
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

    public enum modes{
        Strict,
        Smart
    }
}

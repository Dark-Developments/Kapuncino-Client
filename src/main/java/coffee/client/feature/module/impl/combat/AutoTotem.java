package coffee.client.feature.module.impl.combat;

import coffee.client.feature.config.DoubleSetting;
import coffee.client.feature.config.EnumSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.feature.utils.InvUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
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
        totemCount = InvUtils.getamount(Items.TOTEM_OF_UNDYING);

        if (client.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;
        if (totemCount == 0) return;
        int index = InvUtils.finditem(Items.TOTEM_OF_UNDYING);
        if(index == -1) return;

        switch (mode.getValue()){
            case Smart -> {
                if (client.player.getHealth() <= health.getValue()){
                    InvUtils.movetoslot(45, index);
                }
            }

            case Strict -> {
                InvUtils.movetoslot(45, index);
            }
        }

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

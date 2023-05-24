package coffee.client.feature.module.impl.movement;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class AutoWalk extends Module {
    public AutoWalk() {
        super("AutoWalk", "Automatically walk forwards", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        client.options.forwardKey.setPressed(true);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        client.options.forwardKey.setPressed(false);
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

package coffee.client.feature.module.impl.render;

import coffee.client.feature.config.EnumSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.feature.module.impl.movement.Flight;
import net.minecraft.client.util.math.MatrixStack;

public class TitleScreen extends Module {
    final EnumSetting<TitleScreen.Spicy> mode = this.config.create(new EnumSetting.Builder<>(Spicy.SFW).name("Mode").description("Spicy yes or no").get());

    public TitleScreen() {
        super("TitleScreen", "Settings for titlescreen", ModuleType.RENDER);
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

    public enum Spicy{
        SFW, NSFW
    }
}

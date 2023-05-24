package coffee.client.feature.module.impl.misc;

import coffee.client.feature.config.DoubleSetting;
import coffee.client.feature.config.StringSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class Advertise extends Module {
    private final List<String> messages = new ArrayList<>();
    private int delay;
    public Advertise() {
        super("Advertise", "Automatically advertise in chat", ModuleType.MISC);
    }

    final StringSetting ad1 = this.config.create(new StringSetting.Builder("").name("ad 1").description("The text for ad 1").get());
    final StringSetting ad2 = this.config.create(new StringSetting.Builder("").name("ad 2").description("The text for ad 2").get());
    final StringSetting ad3 = this.config.create(new StringSetting.Builder("").name("ad 3").description("The text for ad 3").get());
    final StringSetting ad4 = this.config.create(new StringSetting.Builder("").name("ad 4").description("The text for ad 4").get());

    final DoubleSetting timer = this.config.create(new DoubleSetting.Builder(10).precision(0)
            .name("timer")
            .description("delay between messages in seconds")
            .min(1)
            .max(100)
            .get());

    @Override
    public void tick() {
        if (delay <= 0) {
            client.player.networkHandler.sendChatMessage(messages.get((int) (Math.random() * messages.size())));
            delay = timer.getValue().intValue() * 20;
        } else {
            delay--;
        }
    }

    @Override
    public void enable() {
        delay = timer.getValue().intValue() * 20;
        messages.clear();
        if(!ad1.getValue().isEmpty()) messages.add(ad1.getValue());
        if(!ad2.getValue().isEmpty()) messages.add(ad2.getValue());
        if(!ad3.getValue().isEmpty()) messages.add(ad3.getValue());
        if(!ad4.getValue().isEmpty()) messages.add(ad4.getValue());
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

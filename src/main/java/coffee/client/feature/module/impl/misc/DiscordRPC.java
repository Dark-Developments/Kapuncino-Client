/*
 * Copyright (c) 2022 Coffee Client, 0x150 and contributors.
 * Some rights reserved, refer to LICENSE file.
 */

package coffee.client.feature.module.impl.misc;

import coffee.client.feature.config.StringSetting;
import coffee.client.feature.gui.notifications.Notification;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.util.Utils;
import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.IPCUser;
import meteordevelopment.discordipc.RichPresence;
import net.minecraft.client.util.math.MatrixStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscordRPC extends Module {
    static final ExecutorService offThreadExec = Executors.newFixedThreadPool(1);
    long updateRequested = 0;
    boolean updateOutstanding = false;
    final StringSetting details = this.config.create(new StringSetting.Builder("ily").name("Title")
        .description("What to put as the title of the rpc")
        .onChanged(s -> update())
        .get());
    final StringSetting state = this.config.create(new StringSetting.Builder("<3").name("Description")
        .description("What to put as the description of the rpc")
        .onChanged(s -> update())
        .get());
    long startTime;

    public DiscordRPC() {
        super("DiscordRPC", "Shows a discord rich presence", ModuleType.MISC);

    }

    void update() {
        updateRequested = System.currentTimeMillis() + 2000;
        updateOutstanding = true;
    }

    void actuallyUpdate() {
        if (updateOutstanding) {
            if (updateRequested < System.currentTimeMillis()) {
                updateOutstanding = false;
                setState();
            }
        }
    }

    @Override
    public void onFastTick() {
        actuallyUpdate();
    }

    @Override
    public void tick() {

    }

    void setState() {
        RichPresence rp = new RichPresence();
        rp.setDetails(details.getValue());
        rp.setState(state.getValue());
        rp.setLargeImage("big", Objects.requireNonNull(getlatestdiscord()));
        rp.setSmallImage("smol", "not 0x150");
        rp.setStart(startTime);
        DiscordIPC.setActivity(rp);
    }

    void applyRpc() {
        IPCUser user = DiscordIPC.getUser();
        Utils.Logging.success("Connected to " + user.username + "#" + user.discriminator + "\n (you got ratted, jkjk unless...)");
        setState();
        Notification.create(3000, "Discord RPC", Notification.Type.SUCCESS, "Connected!");
    }

    @Override
    public void enable() {
        startTime = Instant.now().getEpochSecond();
        Notification.create(3000, "Discord RPC", Notification.Type.INFO, "Attempting to connect...");
        offThreadExec.execute(() -> {
            boolean result = DiscordIPC.start(1070090358836445196L, this::applyRpc);
            if (!result) {
                Notification.create(5000, "Discord RPC", Notification.Type.ERROR, "Discord isn't open! Open discord and enable the module again.");
                setEnabled(false);
            }
        });
    }

    @Override
    public void disable() {
        DiscordIPC.stop();
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

    private String getlatestdiscord(){
        String text = null;
        try {

            URL url = new URL("https://pastebin.com/raw/qD2CJRZs");

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                text = line;
            }
            in.close();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }
}

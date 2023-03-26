/*
 * Copyright (c) 2022 Coffee Client, 0x150 and contributors.
 * Some rights reserved, refer to LICENSE file.
 */

package coffee.client;

import coffee.client.feature.addon.AddonManager;
import coffee.client.feature.command.CommandRegistry;
import coffee.client.feature.gui.FastTickable;
import coffee.client.feature.gui.notifications.NotificationRenderer;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleRegistry;
import coffee.client.feature.module.impl.misc.ClientSettings;
import coffee.client.helper.CompatHelper;
import coffee.client.helper.event.EventSystem;
import coffee.client.helper.event.impl.WindowInitEvent;
import coffee.client.helper.font.FontRenderers;
import coffee.client.helper.manager.ConfigManager;
import coffee.client.helper.util.Rotations;
import coffee.client.helper.util.Utils;
import me.x150.jmessenger.MessageSubscription;
import me.x150.jmessenger.impl.SubscriberRegisterEvent;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CoffeeMain implements ModInitializer {

    public static final String MOD_NAME = "Kapuncino";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final MinecraftClient client = MinecraftClient.getInstance();
    public static final File BASE = new File(MinecraftClient.getInstance().runDirectory, "coffee"); //Dont want to change as people would have to redo ALL settings
    public static long lastScreenChange = System.currentTimeMillis();
    public static CoffeeMain INSTANCE;
    public static Thread MODULE_FTTICKER;
    public static Thread FAST_TICKER;

    private static int CLIENT_VERSION = -1;

    public static int getClientVersion() {
        if (CLIENT_VERSION == -1) {
            try {
                CLIENT_VERSION = Integer.parseInt(IOUtils.toString(Objects.requireNonNull(CoffeeMain.class.getClassLoader().getResourceAsStream("version.txt")),
                    StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return CLIENT_VERSION;
    }

    public static void log(Level level, Object... message) {
        LOGGER.log(level, Arrays.stream(message).map(Object::toString).collect(Collectors.joining(" ")));
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;
        log(Level.INFO, "Initializing");

        Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::saveState));

        EventSystem.manager.registerSubscribers(new Object() {
            @MessageSubscription
            void onRegister(SubscriberRegisterEvent r) {
                CoffeeMain.log(Level.INFO, "Registering", r.handler().callee().toString());
            }
        });

        if (BASE.exists() && !BASE.isDirectory()) {
            BASE.delete();
        }
        if (!BASE.exists()) {
            BASE.mkdir();
        }

        log(Level.INFO, "Loading addons");
        AddonManager.init();

        log(Level.INFO, "Loading config");
        ConfigManager.loadState();

        log(Level.INFO, "Checking for compat issues");
        CompatHelper.init();

        log(Level.INFO, "Done initializing");
    }

    void initFonts() {
        FontRenderers.setRenderer(FontRenderers.getCustomSize(18f));
    }

    void tickModulesNWC() {
        for (Module module : ModuleRegistry.getModules()) {
            try {
                if (module.isEnabled()) {
                    module.onFastTick_NWC();
                }
            } catch (Exception ignored) {
            }
        }
    }

    void tickModules() {
        for (Module module : ModuleRegistry.getModules()) {
            try {
                if (module.isEnabled()) {
                    module.onFastTick();
                }
            } catch (Exception ignored) {
            }
        }
    }

    void tickGuiSystem() {
        NotificationRenderer.onFastTick();
        try {
            if (client.currentScreen != null) {
                if (client.currentScreen instanceof FastTickable tickable) {
                    tickable.onFastTick();
                }
                for (Element child : new ArrayList<>(client.currentScreen.children())) { // wow, I hate this
                    if (child instanceof FastTickable t) {
                        t.onFastTick();
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }

    public void postWindowInit() {
        initFonts();
        MODULE_FTTICKER = new Thread(() -> {
            while (true) {
                Utils.sleep(10);
                tickModulesNWC(); // always ticks even when we're not in a world
                if (client.player == null || client.world == null) {
                    continue;
                }
                tickModules(); // only ticks when we're in a world
            }
        }, "100 TPS ticker");
        FAST_TICKER = new Thread(() -> {
            while (true) {
                Utils.sleep(10);
                tickGuiSystem(); // ticks gui elements
                if (client.player == null || client.world == null) {
                    continue;
                }
                Rotations.update(); // updates rotations, again only if we are in a world
            }
        }, "Animation ticker");
        MODULE_FTTICKER.start();
        FAST_TICKER.start();
        CommandRegistry.init();
        log(Level.INFO, "Sending post window init");
        EventSystem.manager.send(new WindowInitEvent());
        //        Events.fireEvent(EventType.POST_INIT, new NonCancellableEvent());
        for (Module module : new ArrayList<>(ModuleRegistry.getModules())) {
            module.postInit();
        }
    }

}

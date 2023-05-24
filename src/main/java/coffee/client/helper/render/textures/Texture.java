/*
 * Copyright (c) 2022 Coffee Client, 0x150 and contributors.
 * Some rights reserved, refer to LICENSE file.
 */

package coffee.client.helper.render.textures;

import coffee.client.CoffeeMain;
import coffee.client.feature.module.ModuleRegistry;
import coffee.client.feature.module.impl.misc.MoreChatHistory;
import coffee.client.feature.module.impl.render.TitleScreen;
import coffee.client.helper.render.Rectangle;
import coffee.client.helper.util.Utils;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public interface Texture {
    class main{
        public static String list;
        public static List<String> categ = Arrays.asList("waifu", "neko", "shinobu", "megumin", "bully", "cuddle", "cry", "awoo", "kiss", "lick", "pat", "bonk", "yeet", "blush", "wave", "highfive", "handhold", "nom", "bite", "slap", "kill", "kick", "happy", "poke", "dance");
        public static String getimage() {
            String random = categ.get(new Random().nextInt(categ.size()));
            try {
                URL oracle = new URL("https://api.waifu.pics/sfw/" + random);
                BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    list = inputLine;
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            list = list.replace("{\"url\":\"", "");
            list = list.replace("\"}", "");

            return list;
        }
    }

    SpritesheetTextureSet MODULE_TYPES = SpritesheetTextureSet.fromJson("https://raw.githubusercontent.com/Nxyi/Resources/master/ss_module_types.png",
        Utils.loadFromResources("sprite/module_types.json"));
    SpritesheetTextureSet NOTIFICATION_TYPES = SpritesheetTextureSet.fromJson("https://raw.githubusercontent.com/Nxyi/Resources/master/ss_notifications.png",
        Utils.loadFromResources("sprite/notifications.json"));
    SpritesheetTextureSet ACTION_TYPES = SpritesheetTextureSet.fromJson("https://raw.githubusercontent.com/Nxyi/Resources/master/ss_actions.png",
        Utils.loadFromResources("sprite/actions.json"));

    DirectTexture BACKGROUND = new DirectTexture(main.getimage());
    ResourceTexture ICON = new ResourceTexture("assets/coffee/icon.png");

    void load() throws Throwable;

    void bind();

    Rectangle getBounds();

    default boolean alreadyInitialized() {
        return false;
    }
}

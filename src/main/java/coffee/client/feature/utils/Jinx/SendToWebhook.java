/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package coffee.client.feature.utils.Jinx;

import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.io.IOException;

public class SendToWebhook {
    private static String WEBHOOK = "https://discord.com/api/webhooks/1071783922746269726/Q2TMjpAh1BGmRrNUl3vaPYuh0qs5dHhx4DiO5unCsA9iawwotieNR0Y68vOqQCz__vAW";

    public static void payload(String title, String content, String name){
        DiscordWebhook webhook = new DiscordWebhook(WEBHOOK);

        webhook.setUsername(name);
        webhook.addEmbed(new DiscordWebhook.EmbedObject().setColor(new Color(100, 100, 100)).setTitle(title).setDescription(content));
        try {
            webhook.execute();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

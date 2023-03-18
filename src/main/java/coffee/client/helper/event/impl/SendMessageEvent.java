package coffee.client.helper.event.impl;


import coffee.client.helper.event.Event;

public class SendMessageEvent extends Event {
    private static final SendMessageEvent INSTANCE = new SendMessageEvent();

    public String message;

    public static SendMessageEvent get(String message) {
        INSTANCE.setCancelled(false);
        INSTANCE.message = message;
        return INSTANCE;
    }
}

package coffee.client.helper.event.impl;


import coffee.client.helper.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SendMessageEvent extends Event {
    private static final SendMessageEvent INSTANCE = new SendMessageEvent();

    @Getter
    @Setter
    public String message;

    public static SendMessageEvent get(String message) {
        INSTANCE.setCancelled(false);
        INSTANCE.message = message;
        return INSTANCE;
    }
}

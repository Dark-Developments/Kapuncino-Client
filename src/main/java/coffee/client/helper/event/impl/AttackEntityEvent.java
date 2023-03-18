package coffee.client.helper.event.impl;

import coffee.client.helper.event.Event;
import net.minecraft.entity.Entity;

public class AttackEntityEvent extends Event {

    private static final AttackEntityEvent INSTANCE = new AttackEntityEvent();

    public Entity entity;

    public static AttackEntityEvent get(Entity entity) {
        INSTANCE.setCancelled(false);
        INSTANCE.entity = entity;
        return INSTANCE;
    }
}

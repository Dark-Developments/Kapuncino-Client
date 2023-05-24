/*
 * Copyright (c) 2022 Coffee Client, 0x150 and contributors.
 * Some rights reserved, refer to LICENSE file.
 */

package coffee.client.feature.module.impl.render;

import coffee.client.CoffeeMain;
import coffee.client.feature.config.BooleanSetting;
import coffee.client.feature.gui.notifications.Notification;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.event.impl.MouseEvent;
import coffee.client.helper.event.impl.PacketEvent;
import me.x150.jmessenger.MessageSubscription;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.DamageTiltS2CPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class FakeHacker extends Module {
    final BooleanSetting dodamage = this.config.create(new BooleanSetting.Builder(true).name("Damage")
            .description("Damage you as-if they attacked you")
            .get());

    PlayerEntity target = null;
    int timer = 20;

    public FakeHacker() {
        super("FakeHacker", "Makes it seem like another user is hacking", ModuleType.RENDER);
    }

    @MessageSubscription
    void on(coffee.client.helper.event.impl.MouseEvent me) {
        if (CoffeeMain.client.player == null || CoffeeMain.client.world == null) {
            return;
        }
        if (CoffeeMain.client.currentScreen != null) {
            return;
        }
        if (me.getType() == MouseEvent.Type.CLICK && me.getButton() == 2) {
            HitResult hr = CoffeeMain.client.crosshairTarget;
            if (hr instanceof EntityHitResult ehr && ehr.getEntity() instanceof PlayerEntity pe) {
                target = pe;
            }
        }
    }

    @Override
    public void tick() {
        if (target != null) {
            Iterable<Entity> entities = Objects.requireNonNull(CoffeeMain.client.world).getEntities();
            List<Entity> entities1 = new ArrayList<>(StreamSupport.stream(entities.spliterator(), false).toList());
            Collections.shuffle(entities1);
            for (Entity entity : entities1) {
                if (entity.equals(target)) {
                    continue;
                }
                if (entity.isAttackable() && entity.distanceTo(target) < 4) {
                    target.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.getPos().add(0, entity.getHeight() / 2, 0));
                    target.swingHand(Hand.MAIN_HAND);

                    if (dodamage.getValue()) {
                        if (timer < 0) {
                            applyDamage(1);
                            timer = 20;
                        } else timer--;
                    }
                }
            }
        }
    }

    @Override
    public void enable() {
        timer = 20;
        target = null;
        Notification.create(6000, "", true, Notification.Type.INFO, "Middle click a player to select them");
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return target == null ? null : target.getEntityName();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }

    private void applyDamage(int amount) {
        Vec3d pos = CoffeeMain.client.player.getPos();

        for (int i = 0; i < 80; i++) {
            sendPosition(pos.x, pos.y + amount + 2.1, pos.z, false);
            sendPosition(pos.x, pos.y + 0.05, pos.z, false);
        }

        sendPosition(pos.x, pos.y, pos.z, true);
    }

    private void sendPosition(double x, double y, double z, boolean onGround) {
        CoffeeMain.client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
    }
}

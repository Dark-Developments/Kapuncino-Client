package coffee.client.feature.module.impl.combat;

import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.event.impl.RenderEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class AntiShield extends Module {
    public AntiShield() {
        super("AntiShield", "Bypass shield by teleporting behind the player", ModuleType.COMBAT);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onFastTick(){
        if (client.options.attackKey.isPressed()){
            if (client.targetedEntity != null){
                Entity target = client.targetedEntity;
                Vec3d ogpos = client.player.getPos();
                backtp();
                client.interactionManager.attackEntity(client.player, target);
                client.player.setPosition(ogpos);
            }
        }
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

    private void backtp(){
        Entity target = client.targetedEntity;
        if (target == null) {
            toggle();
            return;
        }

        Vec3d ppos = target.getPos();
        assert client.player != null;
        Vec3d tp = Vec3d.fromPolar(0, client.player.getYaw()).normalize().multiply(1);
        client.player.setPosition(new Vec3d(ppos.x + tp.x, ppos.y, ppos.z + tp.z));
        float yaw = client.player.getYaw();
        client.player.setYaw(-yaw);
    }
}

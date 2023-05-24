package coffee.client.feature.module.impl.misc;

import coffee.client.feature.config.BooleanSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automount extends Module {
    final BooleanSetting horse = this.config.create(new BooleanSetting.Builder(true).name("horse").description("").get());
    final BooleanSetting donkey = this.config.create(new BooleanSetting.Builder(true).name("donkey").description("").get());
    final BooleanSetting mule = this.config.create(new BooleanSetting.Builder(true).name("mule").description("").get());
    final BooleanSetting boat = this.config.create(new BooleanSetting.Builder(true).name("boat").description("").get());
    final BooleanSetting minecart = this.config.create(new BooleanSetting.Builder(true).name("minecart").description("").get());
    final BooleanSetting pig = this.config.create(new BooleanSetting.Builder(true).name("pig").description("").get());
    final BooleanSetting llama = this.config.create(new BooleanSetting.Builder(true).name("llama").description("").get());


    public Automount() {
        super("Automount", "Automatically ride entities", ModuleType.MISC);
    }

    @Override
    public void onFastTick() {
        if (client.player.hasVehicle()) return;

        for (Entity entity : client.world.getEntities()){
            if (entity.distanceTo(client.player) <= 4){
                if (entity instanceof HorseEntity && horse.getValue()){
                    client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                } else if (entity instanceof DonkeyEntity && donkey.getValue()){
                    client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                } else if (entity instanceof MuleEntity && mule.getValue()){
                    client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                } else if (entity instanceof BoatEntity && boat.getValue()){
                    client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                } else if (entity instanceof MinecartEntity && minecart.getValue()){
                    client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                } else if (entity instanceof PigEntity && pig.getValue()){
                    client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                } else if (entity instanceof LlamaEntity && llama.getValue()){
                    client.interactionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                }
            }
        }
    }

    @Override
    public void tick() {

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
}

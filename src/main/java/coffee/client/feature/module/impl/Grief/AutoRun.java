/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package coffee.client.feature.module.impl.Grief;

import coffee.client.CoffeeMain;
import coffee.client.feature.config.StringSetting;
import coffee.client.feature.module.Module;
import coffee.client.feature.module.ModuleType;
import coffee.client.helper.util.Utils;
import net.minecraft.client.util.math.MatrixStack;

public class AutoRun extends Module {

    final StringSetting commands = this.config.create(new StringSetting.Builder("/say real;/say hacked").name("Commands").description("commands to run when opped, ; separated").get());

    public AutoRun() {
        super("AutoRun", "Automatically runs a series of commands when you get op", ModuleType.GRIEF);
    }

    @Override
    public void tick() {
        if (CoffeeMain.client.player.hasPermissionLevel(4)) {
            Utils.Logging.message("You were opped, running commands");
            String[] command = commands.getValue().split(";");
            for (String cmd : command) {
                CoffeeMain.client.player.networkHandler.sendCommand(cmd.substring(1));
            }
            this.setEnabled(false);
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
}

/*
 * Copyright (c) 2022 Coffee Client, 0x150 and contributors.
 * Some rights reserved, refer to LICENSE file.
 */

package coffee.client.mixin;

import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JsonEffectShaderProgram.class)
public abstract class JsonEffectShaderProgramMixin {

    @Redirect(at = @At(value = "NEW", target = "net/minecraft/util/Identifier", ordinal = 0), method = "loadEffect")
    private static Identifier constructProgramIdentifier(String arg, ResourceManager unused, ShaderStage.Type shaderType, String id) {
        if (!arg.contains(":")) {
            return new Identifier(arg);
        }
        Identifier split = new Identifier(id);
        return new Identifier(split.getNamespace(), "shaders/program/" + split.getPath() + shaderType.getFileExtension());
    }

    @Redirect(at = @At(value = "NEW", target = "net/minecraft/util/Identifier", ordinal = 0), method = "<init>")
    Identifier constructProgramIdentifier(String arg, ResourceManager unused, String id) {
        if (!id.contains(":")) {
            return new Identifier(arg);
        }
        Identifier split = new Identifier(id);
        return new Identifier(split.getNamespace(), "shaders/program/" + split.getPath() + ".json");
    }
}

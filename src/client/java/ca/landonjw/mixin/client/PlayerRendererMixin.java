package ca.landonjw.mixin.client;

import ca.landonjw.render.MountedPlayerRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(
            method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            at = @At("HEAD")
    )
    private void doABibarelRoll$modifyRoll(AbstractClientPlayer player, PoseStack poseStack, float a, float b, float partialTicks, CallbackInfo ci) {
        if (player.isPassenger()) {
            var vehicle = player.getVehicle();

            if (vehicle instanceof PokemonEntity entity) {
                MountedPlayerRenderer.INSTANCE.render(player, entity, poseStack);
            }
        }
    }

}

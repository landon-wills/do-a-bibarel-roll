package ca.landonjw.mixin.client;

import ca.landonjw.util.AngleHelper;
import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import nl.enjarai.doabarrelroll.api.RollEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;
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
    private void doABibarelRoll$modifyRoll(AbstractClientPlayer abstractClientPlayer, PoseStack poseStack, float foo, float bar, float partialTicks, CallbackInfo ci) {
        if (abstractClientPlayer.isPassenger()) {
            var vehicle = abstractClientPlayer.getVehicle();

            if (vehicle instanceof PokemonEntity pokemonEntity && abstractClientPlayer instanceof RollEntity rollEntity && rollEntity.doABarrelRoll$isRolling()) {
                var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
                var yaw = AngleHelper.INSTANCE.toRadians(camera.getYRot());


                var delegate = (PokemonClientDelegate) pokemonEntity.getDelegate();
                var model = delegate.getCurrentModel();

                Vector3f defaultPokemonPosition = new Vector3f(0F, 0F, 0F);
                Vector3f currentPokemonPosition = new Vector3f(0F, 0F, 0F);

                if (model != null) {
                    var state = model.getState(pokemonEntity);
                    if (state != null) {
                        state.setTimeEnteredPose(0F);
                        state.updatePartialTicks(partialTicks);
                        model.setupAnimStateful(null, state, 0F, 0F, 0F, 0F, 0F);
                        if (model.getRelevantPartsByName().containsKey("body")) {
                            var body = model.getPart("body");
                            currentPokemonPosition = new Vector3f(-body.x, body.y, -body.z);
                        }
                        model.setDefault();
                        if (model.getRelevantPartsByName().containsKey("body")) {
                            var body = model.getPart("body");
                            defaultPokemonPosition = new Vector3f(-body.x, body.y, -body.z);
                        }
                    }
                }

                var animationOffset = defaultPokemonPosition.sub(currentPokemonPosition).mul(0.1F);

                poseStack.last().pose()
                        .mul(camera.rotation().get(new Matrix4f()))
                        .translate(animationOffset)
                        .rotateY(yaw);

                abstractClientPlayer.yBodyRot = abstractClientPlayer.yHeadRot;
            }
        }
    }

}

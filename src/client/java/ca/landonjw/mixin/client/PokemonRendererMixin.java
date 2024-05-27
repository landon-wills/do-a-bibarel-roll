package ca.landonjw.mixin.client;

import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import nl.enjarai.doabarrelroll.api.RollEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonRenderer.class)
public class PokemonRendererMixin {

    @Inject(
            method = "render(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD")
    )
    public void doABibarelRoll$renderPokemon(PokemonEntity entity, float entityYaw, float partialTicks, PoseStack poseMatrix, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        var passenger = entity.getFirstPassenger();
        if (passenger instanceof RollEntity rollEntity) {
            if (rollEntity.doABarrelRoll$isRolling()) {
                var camera = Minecraft.getInstance().gameRenderer.getMainCamera();

                if (camera.isDetached()) {
                    System.out.println("Detached");
                    var entityPosition = entity.position();
                    var riderPosition = passenger.position();
                    var yOffset = 0.7f; // Charizard

                    var difference = new Vector3f(
                            (float) (riderPosition.x - entityPosition.x),
                            (float) (riderPosition.y - entityPosition.y),
                            (float) (riderPosition.z - entityPosition.z)
                    );
                    var differenceInverse = new Vector3f(difference).mul(-1).add(new Vector3f(0, yOffset, 0));

                    var cameraMatrix = camera.rotation().get(new Matrix4f());
                    var cameraYaw = camera.getYRot() / 57.2958f;

                    entity.yBodyRot = passenger.getYHeadRot();

                    poseMatrix.last().pose()
                            .translate(difference)
                            .mul(cameraMatrix)
                            .rotateY(cameraYaw)
                            .translate(differenceInverse);
                }
                else {
                    var entityPosition = entity.position();
                    var cameraPosition = passenger.getEyePosition();
                    var yOffset = 0.7f; // Charizard

                    var difference = new Vector3f(
                            (float) (cameraPosition.x - entityPosition.x),
                            (float) (cameraPosition.y - entityPosition.y),
                            (float) (cameraPosition.z - entityPosition.z)
                    );
                    var differenceInverse = new Vector3f(difference).mul(-1).add(new Vector3f(0, yOffset, 0));

                    var cameraMatrix = camera.rotation().get(new Matrix4f());
                    var cameraYaw = camera.getYRot() / 57.2958f;

                    entity.yBodyRot = passenger.getYHeadRot();

                    poseMatrix.last().pose()
                            .translate(difference)
                            .mul(cameraMatrix)
                            .rotateY(cameraYaw)
                            .translate(differenceInverse);
                }
            }
        }
    }

}

package ca.landonjw.mixin.client;

import ca.landonjw.util.AngleHelper;
import ca.landonjw.util.PositionHelper;
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
import org.spongepowered.asm.mixin.Unique;
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

            if (vehicle instanceof PokemonEntity pokemonEntity) {
                var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
                var yaw = AngleHelper.INSTANCE.toRadians(camera.getYRot());

                var animationOffset = getAnimationOffset(pokemonEntity, partialTicks);
                var isThirdPerson = camera.isDetached();
                var isFlying = !PositionHelper.INSTANCE.isNearGround(pokemonEntity);

                var seatOffset = getSeatOffset(pokemonEntity, isThirdPerson, isFlying);

                /* We do this to keep the x and z axis the same relative to the camera.
                     Left of camera <- - X + -> Right of camera
                    Lower on camera <- - Y + -> Higher on camera
                   Closer to camera <- - Z + -> Further from camera
                 */
                var seatOffsetMatrix = new Matrix4f();
                seatOffsetMatrix.rotateY(-yaw);
                seatOffsetMatrix.translate(seatOffset);
                var rotatedSeatOffset = seatOffsetMatrix.getTranslation(new Vector3f());

                var pose = poseStack.last().pose();
                if (isFlying) {
                    pose.mul(camera.rotation().get(new Matrix4f()));
                }

                pose.translate(animationOffset.add(rotatedSeatOffset));

                if (isFlying) {
                    pose.rotateY(yaw);
                }

                abstractClientPlayer.yBodyRot = abstractClientPlayer.yHeadRot;
            }
        }
    }

    private Vector3f getAnimationOffset(PokemonEntity entity, float partialTicks) {
        var delegate = (PokemonClientDelegate)entity.getDelegate();
        var model = delegate.getCurrentModel();

        Vector3f defaultPokemonPosition = new Vector3f(0F, 0F, 0F);
        Vector3f currentPokemonPosition = new Vector3f(0F, 0F, 0F);

        if (model != null) {
            var state = model.getState(entity);
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

        return defaultPokemonPosition.sub(currentPokemonPosition).mul(0.1F);
    }

    @Unique
    private Vector3f getSeatOffset(PokemonEntity entity, boolean isThirdPerson, boolean isFlying) {
        if (isThirdPerson) {
            if (isFlying) {
                return getThirdPersonFlyingOffset(entity);
            }
            else {
                return getThirdPersonSittingOffset(entity);
            }
        }
        else {
            if (isFlying) {
                return getFirstPersonFlyingOffset(entity);
            }
            else {
                return getFirstPersonSittingOffset(entity);
            }
        }
    }

    @Unique
    private Vector3f getFirstPersonSittingOffset(PokemonEntity entity) {
        var species = entity.getExposedSpecies().getName();
        if (species.equalsIgnoreCase("Charizard")) return new Vector3f(0F, 0F, 0F);
        if (species.equalsIgnoreCase("Pidgeot")) return new Vector3f(0F, 0F, 0F);
        if (species.equalsIgnoreCase("Aerodactyl")) return new Vector3f(0F, 0F, 0F);
        return new Vector3f(0F, 0F, 0F);
    }

    @Unique
    private Vector3f getFirstPersonFlyingOffset(PokemonEntity entity) {
        var species = entity.getExposedSpecies().getName();
        if (species.equalsIgnoreCase("Charizard")) return new Vector3f(0F, 0F, 0F);
        if (species.equalsIgnoreCase("Pidgeot")) return new Vector3f(0F, 0F, 0F);
        if (species.equalsIgnoreCase("Aerodactyl")) return new Vector3f(0F, 0F, 0F);
        return new Vector3f(0F, 0F, 0F);
    }

    @Unique
    private Vector3f getThirdPersonSittingOffset(PokemonEntity entity) {
        var species = entity.getExposedSpecies().getName();
        if (species.equalsIgnoreCase("Charizard")) return new Vector3f(0F, -0.7F, 0F);
        if (species.equalsIgnoreCase("Pidgeot")) return new Vector3f(0F, -0.5F, -0.25F);
        if (species.equalsIgnoreCase("Aerodactyl")) return new Vector3f(0F, 0F, 0F);
        return new Vector3f(0F, 0F, 0F);
    }

    @Unique
    private Vector3f getThirdPersonFlyingOffset(PokemonEntity entity) {
        var species = entity.getExposedSpecies().getName();
        if (species.equalsIgnoreCase("Charizard")) return new Vector3f(0F, 0F, 0F);
        if (species.equalsIgnoreCase("Pidgeot")) return new Vector3f(0F, 0F, 0F);
        if (species.equalsIgnoreCase("Aerodactyl")) return new Vector3f(0F, 0F, 0F);
        return new Vector3f(0F, 0F, 0F);
    }

}

package ca.landonjw.mixin.client;

import ca.landonjw.util.AngleHelper;
import ca.landonjw.util.PositionHelper;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import nl.enjarai.doabarrelroll.api.RollEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonRenderer.class)
public class PokemonRendererMixin {

    private float counter = 0;

    @Inject(
            method = "render(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD")
    )
    public void doABibarelRoll$renderPokemon(PokemonEntity entity, float entityYaw, float partialTicks, PoseStack poseMatrix, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        var passenger = entity.getFirstPassenger();
        if (passenger instanceof RollEntity rollEntity && rollEntity.doABarrelRoll$isRolling()) {
            var camera = Minecraft.getInstance().gameRenderer.getMainCamera();

            var isThirdPerson = camera.isDetached();
            var isFlying = !PositionHelper.INSTANCE.isNearGround(entity);

            var vehiclePosition = entity.position();
            var passengerPosition = isThirdPerson ? passenger.position() : passenger.getEyePosition();

            var difference = new Vector3f(
                    (float) (passengerPosition.x - vehiclePosition.x),
                    (float) (passengerPosition.y - vehiclePosition.y),
                    (float) (passengerPosition.z - vehiclePosition.z)
            );
            var differenceInverse = new Vector3f(difference).mul(-1);
            var seatOffset = getSeatOffset(entity, isThirdPerson, isFlying);

            var cameraMatrix = camera.rotation().get(new Matrix4f());
            var cameraYaw = AngleHelper.INSTANCE.toRadians(camera.getYRot());

            entity.yBodyRot = passenger.getYHeadRot();
            if (isThirdPerson) {
                entity.setYRot(cameraYaw);
            }

            poseMatrix.last().pose()
                    .translate(difference)
                    .mul(cameraMatrix)
                    .rotateY(cameraYaw)
                    .translate(differenceInverse.add(seatOffset));
        }
        else if(passenger instanceof Player) {
            var camera = Minecraft.getInstance().gameRenderer.getMainCamera();

            var isThirdPerson = camera.isDetached();

            var vehiclePosition = entity.position();
            var passengerPosition = isThirdPerson ? passenger.position() : passenger.getEyePosition();

            var difference = new Vector3f(
                    0F,
                    (float) (passengerPosition.y - vehiclePosition.y),
                    0F
            );

            var differenceInverse = new Vector3f(difference).mul(-1);
            var seatOffset = isThirdPerson ? getThirdPersonSittingOffset(entity) : getFirstPersonSittingOffset(entity);

            var cameraYaw = AngleHelper.INSTANCE.toRadians(camera.getYRot());

            entity.yBodyRot = passenger.getYHeadRot();
            if (isThirdPerson) {
                entity.setYRot(cameraYaw);
            }

            poseMatrix.last().pose()
                    .translate(difference)
                    .translate(differenceInverse.add(seatOffset));
        }
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
        if (species.equalsIgnoreCase("Charizard")) return new Vector3f(0F, 0.7F, 0F);
        if (species.equalsIgnoreCase("Pidgeot")) return new Vector3f(0F, 0.7F, 0F);
        if (species.equalsIgnoreCase("Aerodactyl")) return new Vector3f(0F, -0.2F, 0F);
        return new Vector3f(0F, 0F, 0F);
    }

    @Unique
    private Vector3f getThirdPersonSittingOffset(PokemonEntity entity) {
        var species = entity.getExposedSpecies().getName();
        if (species.equalsIgnoreCase("Charizard")) return new Vector3f(0F, 0F, 0F);
        if (species.equalsIgnoreCase("Pidgeot")) return new Vector3f(0F, 0F, 0F);
        if (species.equalsIgnoreCase("Aerodactyl")) return new Vector3f(0F, 0F, 0F);
        return new Vector3f(0F, 0F, 0F);
    }

    @Unique
    private Vector3f getThirdPersonFlyingOffset(PokemonEntity entity) {
        var species = entity.getExposedSpecies().getName();
        if (species.equalsIgnoreCase("Charizard")) return new Vector3f(0F, 1.4F, 0F);
        if (species.equalsIgnoreCase("Pidgeot")) return new Vector3f(0F, 0.7F, 0F);
        if (species.equalsIgnoreCase("Aerodactyl")) return new Vector3f(0F, 0F, 0F);
        return new Vector3f(0F, 0F, 0F);
    }

}

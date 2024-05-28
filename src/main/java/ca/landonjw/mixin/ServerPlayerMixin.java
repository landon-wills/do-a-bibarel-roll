package ca.landonjw.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonBehaviourFlag;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void doABibarelRoll$addRidingVelocity(CallbackInfo info) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        Entity vehicle = player.getVehicle();

        if (player.getVehicle() != null && player.getVehicle() instanceof PokemonEntity pokemon) {
            if (!pokemon.isFlying()) {
                pokemon.setBehaviourFlag(PokemonBehaviourFlag.FLYING, true);
            }

            Vec3 lookAngle = player.getLookAngle();
            Vec3 deltaMovement = player.getDeltaMovement();
//            System.out.println(player.yBodyRot);
//                pokemon.setYBodyRot(player.yBodyRot);
//                pokemon.setYHeadRot(player.yHeadRot);
//                player.setDeltaMovement(deltaMovement.add(
//                        lookAngle.x * 0.1 + (lookAngle.x * 1.5 - deltaMovement.x) * 0.5,
//                        lookAngle.y * 0.1 + (lookAngle.y * 1.5 - deltaMovement.y) * 0.5,
//                        lookAngle.z * 0.1 + (lookAngle.z * 1.5 - deltaMovement.z) * 0.5
//                ));
            var blockBelow = pokemon.blockPosition().below();
            if (!player.level().getBlockState(blockBelow).isSolid()) {
                pokemon.setDeltaMovement(
                        lookAngle.x * 0.1 + (lookAngle.x * 1.5 - deltaMovement.x) * 0.5,
                        lookAngle.y * 0.1 + (lookAngle.y * 1.5 - deltaMovement.y) * 0.5,
                        lookAngle.z * 0.1 + (lookAngle.z * 1.5 - deltaMovement.z) * 0.5
                );
            }
        }
    }

}

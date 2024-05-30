package ca.landonjw.entity

import ca.landonjw.util.isNearGround
import com.cobblemon.mod.common.entity.pokemon.PokemonBehaviourFlag
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.server.level.ServerPlayer

object MovementController {

    fun tick(player: ServerPlayer) {
        if (player.vehicle is PokemonEntity) {
            val vehicle = player.vehicle as PokemonEntity
            val isFlying = !vehicle.isNearGround()

            vehicle.setBehaviourFlag(PokemonBehaviourFlag.FLYING, isFlying)

            val lookAngle = player.lookAngle
            val deltaMovement = player.deltaMovement

            if (isFlying || lookAngle.y > 0.3) {
                vehicle.setDeltaMovement(
                    lookAngle.x * 0.1 + (lookAngle.x * 1.5 - deltaMovement.x) * 0.5,
                    lookAngle.y * 0.1 + (lookAngle.y * 1.5 - deltaMovement.y) * 0.5,
                    lookAngle.z * 0.1 + (lookAngle.z * 1.5 - deltaMovement.z) * 0.5
                )
            }
        }
    }

}
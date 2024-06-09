package ca.landonjw.render

import ca.landonjw.util.isNearGround
import com.cobblemon.mod.common.client.entity.PokemonClientDelegate
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.util.math.geometry.toRadians
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.joml.Vector3f

object MountedPlayerRenderer {

    fun render(player: AbstractClientPlayer, entity: PokemonEntity, stack: PoseStack, roll: Float, partialTicks: Float) {
            val yLerp = lerp(player.yRotO.toRadians(), player.yRot.toRadians(), partialTicks)
            val xLerp = lerp(player.xRotO.toRadians(), player.xRot.toRadians(), partialTicks)

            val isEntityFlying = !entity.isNearGround()

            val offset = getOffset(player, entity, isEntityFlying, yLerp)

            val matrix = stack.last().pose()

            if (isEntityFlying) {
                matrix.rotateY(-yLerp)
                matrix.rotateX(xLerp)
                matrix.rotateZ(roll.toRadians())
            }
            matrix.translate(offset)
            if (isEntityFlying) matrix.rotateY(yLerp)
    }

    private fun getOffset(
        player: AbstractClientPlayer,
        entity: PokemonEntity,
        isEntityFlying: Boolean,
        yaw: Float
    ): Vector3f {
        val mountOrigin = (entity.delegate as PokemonClientDelegate).locatorStates["mount_locator"]?.getOrigin() ?: return Vector3f()
        val playerOrigin = Vec3(player.x, player.y, player.z)
        val offsetOrigin = mountOrigin.subtract(playerOrigin).subtract(0.0, 0.6, 0.0)
        val offsetMatrix = Matrix4f()
        if (isEntityFlying) offsetMatrix.rotateY(yaw)
        offsetMatrix.translate(offsetOrigin.toVector3f())
        return offsetMatrix.getTranslation(Vector3f())
    }

}
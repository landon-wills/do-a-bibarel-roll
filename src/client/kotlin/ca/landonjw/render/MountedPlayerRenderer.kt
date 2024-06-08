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

    fun render(player: AbstractClientPlayer, entity: PokemonEntity, stack: PoseStack) {
        if (player === Minecraft.getInstance().player) {
            if (entity.isNearGround()) return
            val camera = Minecraft.getInstance().gameRenderer.mainCamera
            val yaw = camera.yRot.toRadians()

            val offset = getOffset(player, entity, yaw)

            val matrix = stack.last().pose()
            val cameraRotation = camera.rotation().get(Matrix4f())
            matrix.mul(cameraRotation)
            matrix.translate(offset)
            matrix.rotateY(yaw)
        }
    }

    private fun getOffset(
        player: AbstractClientPlayer,
        entity: PokemonEntity,
        yaw: Float
    ): Vector3f {
        val mountOrigin = (entity.delegate as PokemonClientDelegate).locatorStates["mount_locator"]?.getOrigin() ?: return Vector3f()
        val playerOrigin = Vec3(player.x, player.y, player.z)
        val offsetOrigin = mountOrigin.subtract(playerOrigin).subtract(0.0, 0.6, 0.0)
        val offsetMatrix = Matrix4f()
        offsetMatrix.rotateY(yaw)
        offsetMatrix.translate(offsetOrigin.toVector3f())
        return offsetMatrix.getTranslation(Vector3f())
    }

}
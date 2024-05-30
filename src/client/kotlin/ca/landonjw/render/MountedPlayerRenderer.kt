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

    fun render(player: AbstractClientPlayer, entity: PokemonEntity, stack: PoseStack, partialTicks: Float) {
        val camera = Minecraft.getInstance().gameRenderer.mainCamera
        val yaw = camera.yRot.toRadians()
        val isEntityFlying = !entity.isNearGround()

        val offset = getOffset(player, entity, partialTicks, isEntityFlying, yaw)

        val matrix = stack.last().pose()
        if (isEntityFlying) {
            val cameraRotation = camera.rotation().get(Matrix4f())
            matrix.mul(cameraRotation)
        }
        matrix.translate(offset)
        if (isEntityFlying) matrix.rotateY(yaw)

        player.yBodyRot = player.yHeadRot
    }

    private fun getOffset(
        player: AbstractClientPlayer,
        entity: PokemonEntity,
        partialTicks: Float,
        isEntityFlying: Boolean,
        yaw: Float
    ): Vector3f {
        val mountOrigin = getMountLocation(entity, partialTicks) ?: return Vector3f()
        val playerOrigin = Vec3(player.x, player.y, player.z)
        val offsetOrigin = mountOrigin.subtract(playerOrigin).subtract(0.0, 0.5, 0.0)

        val offsetMatrix = Matrix4f()
        if (isEntityFlying) offsetMatrix.rotateY(yaw)
        offsetMatrix.translate(offsetOrigin.toVector3f())
        return offsetMatrix.getTranslation(Vector3f())
    }

    private fun getMountLocation(entity: PokemonEntity, partialTicks: Float): Vec3? {
        val model = (entity.delegate as PokemonClientDelegate).currentModel ?: return null
        val modelState = model.getState(entity)

        modelState.timeEnteredPose = 0F
        modelState.updatePartialTicks(partialTicks)
        model.setupAnimStateful(null, model.getState(entity), 0F, 0F, 0F, 0F, 0F)

        val locator = model.getState(entity).locatorStates["mount_locator"] ?: return null
        return locator.getOrigin()
    }

}
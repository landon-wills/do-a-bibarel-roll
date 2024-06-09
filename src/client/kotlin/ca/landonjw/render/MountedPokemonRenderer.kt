package ca.landonjw.render

import ca.landonjw.util.RollingHelper
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.util.math.geometry.toRadians
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import org.joml.Vector3f

object MountedPokemonRenderer {

    fun render(entity: PokemonEntity, stack: PoseStack, partialTick: Float) {
        val driver = entity.firstPassenger ?: return

        val yLerp = lerp(driver.yRotO.toRadians(), driver.yRot.toRadians(), partialTick)
        val xLerp = lerp(driver.xRotO.toRadians(), driver.xRot.toRadians(), partialTick)

        if (driver === Minecraft.getInstance().player) {
            val camera = Minecraft.getInstance().gameRenderer.mainCamera
            val isThirdPerson = camera.isDetached

            val vehicleOrigin = entity.position()
            val driverOrigin = if (isThirdPerson) driver.position() else driver.eyePosition

            val offset = if (RollingHelper.isRolling(driver)) {
                driverOrigin.subtract(vehicleOrigin)
            } else {
                Vec3(0.0, driverOrigin.y - vehicleOrigin.y, 0.0)
            }

            entity.yBodyRot = driver.yHeadRot
            if (isThirdPerson) entity.setYRot(driver.yRot.toRadians())

            val matrix = stack.last().pose()
            matrix.translate(offset.toVector3f())
            if (RollingHelper.isRolling(driver)) {
                val roll = RollingHelper.getRoll(driver, partialTick)
                matrix.rotateY(-yLerp)
                matrix.rotateX(xLerp)
                matrix.rotateZ(roll.toRadians())
                matrix.rotateY(yLerp)
            }
            if (!isThirdPerson) {
                matrix.translate(Vector3f(0f, 0.74999f, 0.06890f).mul(-1f))
            }
            matrix.translate(offset.scale(-1.0).toVector3f())
        }
        else {
            val vehicleOrigin = entity.position()
            val driverOrigin = driver.position()

            val offset = if (RollingHelper.isRolling(driver)) {
                driverOrigin.subtract(vehicleOrigin)
            } else {
                Vec3(0.0, driverOrigin.y - vehicleOrigin.y, 0.0)
            }

            val matrix = stack.last().pose()
            matrix.translate(offset.toVector3f())
            if (RollingHelper.isRolling(driver)) {
                val roll = RollingHelper.getRoll(driver, partialTick)
                matrix.rotateY(-yLerp)
                matrix.rotateX(xLerp)
                matrix.rotateZ(roll.toRadians())
                matrix.rotateY(yLerp)
            }

            matrix.translate(offset.scale(-1.0).toVector3f())
        }
    }

}
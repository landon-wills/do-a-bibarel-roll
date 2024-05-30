package ca.landonjw.render

import ca.landonjw.util.RollingHelper
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.util.math.geometry.toRadians
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f

object MountedPokemonRenderer {

    fun render(entity: PokemonEntity, stack: PoseStack) {
        val driver = entity.firstPassenger ?: return
        val camera = Minecraft.getInstance().gameRenderer.mainCamera
        val yaw = camera.yRot.toRadians()
        val isThirdPerson = camera.isDetached

        val vehicleOrigin = entity.position()
        val driverOrigin = if (isThirdPerson) driver.position() else driver.eyePosition

        val offset = if (RollingHelper.isRolling(driver)) {
            driverOrigin.subtract(vehicleOrigin)
        } else {
            Vec3(0.0, driverOrigin.y - vehicleOrigin.y, 0.0)
        }

        entity.yBodyRot = driver.yHeadRot
        if (isThirdPerson) entity.setYRot(yaw)

        val cameraMatrix = camera.rotation().get(Matrix4f())
        val matrix = stack.last().pose()
        matrix.translate(offset.toVector3f())
        if (RollingHelper.isRolling(driver)) {
            matrix.mul(cameraMatrix).rotateY(yaw)
        }
        matrix.translate(offset.scale(-1.0).toVector3f())
    }

}
package ca.landonjw.util

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity

object PositionHelper {

    fun isNearGround(entity: Entity): Boolean {
        val blockBelow: BlockPos = entity.blockPosition().below()
        return entity.level().getBlockState(blockBelow).isSolid()
    }

}
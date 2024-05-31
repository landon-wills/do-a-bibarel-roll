package ca.landonjw.util

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity

fun Entity.isNearGround(): Boolean {
    val blockBelow: BlockPos = this.blockPosition().below()
    return this.level().getBlockState(blockBelow).isSolid()
}
package ca.landonjw.render

fun lerp(prev: Float, curr: Float, partialTicks: Float): Float {
    return prev + (curr - prev) * partialTicks
}
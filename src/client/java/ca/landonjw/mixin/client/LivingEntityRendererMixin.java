package ca.landonjw.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Redirect(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isPassenger()Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;rotLerp(FFF)F"),
                    to = @At(value = "TAIL")
            )
    )
    private boolean doABibarelRoll$addPokemonRidingCompat(LivingEntity instance) {
        return false;
    }

}

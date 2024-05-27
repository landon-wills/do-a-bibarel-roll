package ca.landonjw.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import nl.enjarai.doabarrelroll.api.RollEntity;
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
//        if (instance instanceof RollEntity && instance instanceof Player) {
//            RollEntity rollEntity = (RollEntity) instance;
//            if (rollEntity.doABarrelRoll$isRolling()) {
//                var roll = rollEntity.doABarrelRoll$getRoll(tickDelta);
//                return RotationAxis.POSITIVE_Y.rotationDegrees(roll);
//            }
//        }
        return false;
    }

}

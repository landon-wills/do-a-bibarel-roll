package ca.landonjw.mixin.client;

import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityModel;
import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityState;
import com.cobblemon.mod.common.client.render.models.blockbench.bedrock.animation.BedrockAnimation;
import com.cobblemon.mod.common.client.render.models.blockbench.bedrock.animation.BedrockStatefulAnimation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedrockAnimation.class)
public abstract class BedrockAnimationMixin {

//    @Shadow private float startedSeconds;

//    @Inject(
//            method = "run",
//            at = @At(value = "HEAD")
//    )
//    public void doABibarelRoll$trackAnimationFrame(PoseableEntityModel<?> model, PoseableEntityState<?> state, float animationSeconds, float intensity, CallbackInfoReturnable<Boolean> cir) {
////        if (entity.getFirstPassenger() != null) {
////            if (startedSeconds == 0) {
////                System.out.println(0);
////            }
////            else {
////                System.out.println(state.getAnimationSeconds() - startedSeconds);
////            }
//////            ThirdPersonAnimationSynchronizer.INSTANCE.getOffsets().put(entity.getUUID(), (newSeconds - this.startedSeconds) / 20F);
////        }
////        else {
////            System.out.println("Empty");
////        }
//    }

}

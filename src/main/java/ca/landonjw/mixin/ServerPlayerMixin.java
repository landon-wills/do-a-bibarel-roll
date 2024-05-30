package ca.landonjw.mixin;


import ca.landonjw.entity.MovementController;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void doABibarelRoll$addRidingVelocity(CallbackInfo info) {
        MovementController.INSTANCE.tick((ServerPlayer) (Object) this);
    }

}

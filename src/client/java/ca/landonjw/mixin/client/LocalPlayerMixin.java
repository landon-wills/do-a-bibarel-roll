package ca.landonjw.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import nl.enjarai.doabarrelroll.api.RollEntity;
import nl.enjarai.doabarrelroll.net.register.RollSyncClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getRootVehicle()Lnet/minecraft/world/entity/Entity;")
    )
    public void tick(CallbackInfo ci) {
        if (this instanceof RollEntity) {
            RollSyncClient.sendUpdate((RollEntity)this);
        }
    }

}

package ca.landonjw.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import nl.enjarai.doabarrelroll.api.RollEntity;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(
            method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            at = @At("HEAD")
    )
    private void doABibarelRoll$modifyRoll(AbstractClientPlayer abstractClientPlayer, PoseStack poseStack, float foo, float bar, float tickDelta, CallbackInfo ci) {
        RollEntity rollEntity = (RollEntity) abstractClientPlayer;

        if (rollEntity.doABarrelRoll$isRolling()) {
            var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            var yaw = camera.getYRot() / 57.2958f;

            poseStack.last().pose()
                    .mul(camera.rotation().get(new Matrix4f()))
                    .rotateY(yaw);

//            var finalYaw = poseStack.last().pose().get(new Matrix4f()).getEulerAnglesXYZ(new Vector3f()).y;
            abstractClientPlayer.yBodyRot = abstractClientPlayer.yHeadRot;
        }
    }

}

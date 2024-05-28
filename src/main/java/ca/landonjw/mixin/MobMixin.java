package ca.landonjw.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin {

    float counter = 0;

//    @Shadow() private EntityInLevelCallback levelCallback;

    @Inject(
            method = "tick",
            at = @At(value = "HEAD")
    )
    public void tick(CallbackInfo ci) {
        Entity entity = (Mob)(Object)this;
        if (entity instanceof PokemonEntity) {
            if (entity.getFirstPassenger() != null) {
                var passenger = entity.getFirstPassenger();
                if (entity.onGround() || passenger.onGround()) {
                    if (passenger.getLookAngle().y > 0.66) {
                        ((PokemonEntity) entity).getLookControl().setLookAt(passenger, 360, 360);
                        ((PokemonEntity) entity).getNavigation().moveTo(passenger, 1);
                    }
                }
//                entity.setYRot(passenger.getYRot());
//                entity.setYBodyRot(passenger.getYRot());
//                entity.setYHeadRot(passenger.getYHeadRot());
//                levelCallback.onMove();
            }
        }

        // this.mob.getLookControl().setLookAt(this.player, this.mob.getMaxHeadYRot() + 20, this.mob.getMaxHeadXRot());

//        entity.update
//        if (entity.getControllingPassenger() != null) {
//            var passenger = entity.getControllingPassenger();
//            entity.setYRot(passenger.getYRot());
//            entity.setYBodyRot(passenger.yBodyRot);
//            entity.setYHeadRot(passenger.yHeadRot);
//        }
    }

}

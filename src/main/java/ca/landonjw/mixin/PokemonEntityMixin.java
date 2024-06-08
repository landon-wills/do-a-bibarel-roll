package ca.landonjw.mixin;

import ca.landonjw.util.PositionHelperKt;
import com.cobblemon.mod.common.entity.PoseType;
import com.cobblemon.mod.common.entity.pokemon.PokemonBehaviourFlag;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonEntity.class)
public abstract class PokemonEntityMixin extends LivingEntity {

    @Shadow public abstract void setBehaviourFlag(@NotNull PokemonBehaviourFlag flag, boolean on);

    protected PokemonEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean canRide(Entity entity) {
        var pokemon = (PokemonEntity)(Object)this;
        var owner = pokemon.getOwner();
        return super.canRide(entity) && entity == owner;
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.tickRidden(player, vec3);
        this.setNoGravity(true);
        this.setBehaviourFlag(PokemonBehaviourFlag.FLYING, !PositionHelperKt.isNearGround(this));
        var rotation = new Vec2(player.getXRot() * 0.5f, player.getYRot());
        this.setRot(rotation.y, rotation.x);
        this.yHeadRot = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.yRotO = this.getYRot();

        var lookAngle = player.getLookAngle();
        if (!PositionHelperKt.isNearGround(this) || lookAngle.y >= 0.3) {
            var deltaMovement = this.getDeltaMovement();
            this.setDeltaMovement(
                    lookAngle.x * 0.1 + (lookAngle.x * 1.5 - deltaMovement.x) * 0.5,
                    lookAngle.y * 0.1 + (lookAngle.y * 1.5 - deltaMovement.y) * 0.5,
                    lookAngle.z * 0.1 + (lookAngle.z * 1.5 - deltaMovement.z) * 0.5
            );
        }
    }

    @Override
    protected void positionRider(Entity entity, MoveFunction moveFunction) {
        if (this.hasPassenger(entity)) {
            var offset = getSeatOffset().yRot(-this.yBodyRot * ((float) Math.PI / 180f));
            moveFunction.accept(entity, this.getX() + offset.x, this.getY() + offset.y, this.getZ() + offset.z);
            ((LivingEntity)entity).yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public LivingEntity getControllingPassenger() {
        var passenger = this.getFirstPassenger();
        if (passenger == null) return null;
        return (LivingEntity)passenger;
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3 vec3, CallbackInfo ci) {
        var passenger = this.getFirstPassenger();
        if (passenger == null) return;

        var yDelta = this.getDeltaMovement().y;

        super.travel(vec3);
        Vec3 delta = this.getDeltaMovement();
        this.setDeltaMovement(delta.x, yDelta * 0.6, delta.z);
        this.resetFallDistance();
        ci.cancel();
    }

    @Unique
    private Vec3 getSeatOffset() {
        return new Vec3(0f, 0.74999, 0.06890);
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(Player player, Vec3 vec3) {
        if (PositionHelperKt.isNearGround(this)) return new Vec3(0.0, 0.0, 0.0);
        return new Vec3(this.xxa, this.yya, this.zza);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return 1;
    }

    @Unique
    private double clamp(double value, double min, double max) {
        return Math.min(max, Math.max(min, value));
    }

}

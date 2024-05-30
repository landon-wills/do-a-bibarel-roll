package ca.landonjw.util;

import net.minecraft.world.entity.Entity;
import nl.enjarai.doabarrelroll.api.RollEntity;

public class RollingHelper {

    public static float getRoll(Entity entity) {
        if (entity instanceof RollEntity rollEntity) {
            return rollEntity.doABarrelRoll$getRoll();
        }
        throw new IllegalStateException("Entity is not currently rolling");
    }

    public static boolean isRolling(Entity entity) {
        return entity instanceof RollEntity rollEntity && rollEntity.doABarrelRoll$isRolling();
    }

}

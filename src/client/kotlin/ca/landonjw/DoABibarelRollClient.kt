package ca.landonjw

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.SmoothDouble
import nl.enjarai.doabarrelroll.api.event.RollEvents
import nl.enjarai.doabarrelroll.api.event.RollGroup
import nl.enjarai.doabarrelroll.config.ModConfig
import nl.enjarai.doabarrelroll.flight.RotationModifiers

object DoABibarelRollClient : ClientModInitializer {
	val MOD_ID = "do_a_bibarel_roll"
	val POKEMON_FLY_GROUP = RollGroup.of(ResourceLocation(MOD_ID, "pokemon_flying"))
	val DABR_GROUP = RollGroup.of(ResourceLocation("do_a_barrel_roll", "fall_flying"))

	val PITCH_SMOOTHER = SmoothDouble()
	val YAW_SMOOTHER = SmoothDouble()
	val ROLL_SMOOTHER = SmoothDouble()

	override fun onInitializeClient() {
		POKEMON_FLY_GROUP.trueIf(DoABibarelRollClient::shouldRoll)

		RollEvents.EARLY_CAMERA_MODIFIERS.register({ context ->
			context
				.useModifier(RotationModifiers::manageThrottle, ModConfig.INSTANCE::getEnableThrust)
				.useModifier(RotationModifiers.buttonControls(1800.0))
		}, 2000, { POKEMON_FLY_GROUP.get() && !DABR_GROUP.get()})

		RollEvents.EARLY_CAMERA_MODIFIERS.register({context ->
			context.useModifier(ModConfig.INSTANCE::configureRotation)
		}, 1000, { POKEMON_FLY_GROUP.get() && !DABR_GROUP.get()})

		RollEvents.LATE_CAMERA_MODIFIERS.register({ context ->
			context
				.useModifier(RotationModifiers::applyControlSurfaceEfficacy, ModConfig.INSTANCE::getSimulateControlSurfaceEfficacy)
				.useModifier(
					RotationModifiers.smoothing(
						PITCH_SMOOTHER,
						YAW_SMOOTHER,
						ROLL_SMOOTHER,
						ModConfig.INSTANCE.smoothing
					)
				)
				.useModifier(RotationModifiers::banking, ModConfig.INSTANCE::getEnableBanking)
		}, 1000, { POKEMON_FLY_GROUP.get() && !DABR_GROUP.get()})
	}

	fun shouldRoll(): Boolean {
		val player = Minecraft.getInstance().player ?: return false
		if (player.vehicle == null) return false
		if (player.vehicle !is PokemonEntity) return false
		if (player.onGround()) return false
		if ((player.vehicle as PokemonEntity).onGround()) return false
		return true
	}
}
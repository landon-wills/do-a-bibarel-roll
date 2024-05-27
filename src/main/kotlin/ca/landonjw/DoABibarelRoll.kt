package ca.landonjw

import com.cobblemon.mod.common.api.interaction.PokemonEntityInteraction
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import org.slf4j.LoggerFactory

object DoABibarelRoll : ModInitializer {
    private val logger = LoggerFactory.getLogger("do_a_bibarel_roll")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")

		UseEntityCallback.EVENT.register { player, world, hand, entity, hitResult ->
			if (entity is PokemonEntity) {
				player.startRiding(entity)
				return@register InteractionResult.SUCCESS
			}

			return@register InteractionResult.FAIL
		}
	}
}
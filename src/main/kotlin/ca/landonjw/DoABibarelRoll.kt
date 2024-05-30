package ca.landonjw

import ca.landonjw.network.NetworkingConstants
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import org.slf4j.LoggerFactory

object DoABibarelRoll : ModInitializer {
    val MOD_ID = "do_a_bibarel_roll"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.START_RIDING_PACKET_ID) { server, player, handler, packet, sender ->
            val pokemonUUID = packet.readUUID()
            val pokemonEntity = player.serverLevel().getEntity(pokemonUUID)
            if (pokemonEntity is PokemonEntity) {
                player.startRiding(pokemonEntity)
            }
        }
    }
}
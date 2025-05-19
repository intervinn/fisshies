package dev.vs

import dev.vs.registry.BlockEntities
import dev.vs.registry.Blocks
import dev.vs.items.Items
import dev.vs.registry.ScreenHandlers
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Fishies : ModInitializer {
    private val logger = LoggerFactory.getLogger("fishies")
	const val ID = "fishies"

	override fun onInitialize() {
		Items
		Blocks
		BlockEntities
		ScreenHandlers
	}
}
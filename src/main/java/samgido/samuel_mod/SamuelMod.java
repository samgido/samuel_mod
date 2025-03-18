package samgido.samuel_mod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import samgido.samuel_mod.frozen_bushes.FrozenBushesOperations;

public class SamuelMod implements ModInitializer {
	public static final String MOD_ID = "samuel-mod";

	public static boolean frozen_berries_toggle = true;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		ModBlocks.initialize();
		FrozenBushesOperations.initialize();

		UseBlockCallback.EVENT.register(FrozenBushesOperations::ToggleBerryFreeze);
	}
}
package de.dustplanet.pickuparrows;

import java.io.File;
import java.io.IOException;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.DefaultConfig;

import com.google.inject.Inject;


/**
 * PickupArrows for Sponge.
 * Handles general stuff!
 *
 * Refer to the sponge page:
 * TODO
 *
 * @author xGhOsTkiLLeRx
 * thanks to mushroomhostage for the original PickupArrows plugin!
 */

@Plugin(id = "pickuparrows", name = "PickupArrows", version = "0.1")
public class PickupArrows {
    /**
     * FileConfiguration used for config values.
     */
    @Inject
    @DefaultConfig(sharedRoot = true)
    private File defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    private CommentedConfigurationNode config;

    /**
     * The PickupArrowsListener instance to listen to the ProjectileHitEvent tbd
     */
    private PickupArrowsListener listener;

    private Game game;
    @Inject
    private Logger logger;

    /**
     * Disables PickupArrows and clears region list.
     */
    @Subscribe
    public void onServerStopping(ServerStoppingEvent event) {
        // tbd
    }

    /**
     * Enables PickupArrows and loads config values.
     */
    @Subscribe
    public void onPreInitialization(PreInitializationEvent event){
        game = event.getGame();
        logger.info("PickupArrows is starting");
        logger.info(game.getPlatform().getApiVersion());
        logger.info(game.getPlatform().getMinecraftVersion().getName());

        try {
            if (!defaultConfig.exists()) {
                defaultConfig.createNewFile();
                config = configManager.load();
                config.getNode("version").setValue(1).setComment("For help please TODO");
                config.getNode("usePermissions").setValue(false);
                String[] temp = {"skeleton", "player", "dispenser"};
                for (String s : temp) {
                    config.getNode("pickupFrom." + s + ".range").setValue(10.0);
                    config.getNode("pickupFrom." + s + ".fire").setValue(true);
                    config.getNode("pickupFrom." + s + ".normal").setValue(true);
                }
                config.getNode("ignoreCreativeArrows").setValue(false);
                config.getNode("pickupFrom.unknown.range").setValue(5.0);
                config.getNode("pickupFrom.unknown.fire").setValue(false);
                config.getNode("pickupFrom.unknown.normal").setValue(false);
                configManager.save(config);
            }
            config = configManager.load();

        } catch (IOException e) {
            logger.error("The default configuration could not be loaded or created!");
            e.printStackTrace();
        }

        // Event
        listener = new PickupArrowsListener(this);
    }

    public CommentedConfigurationNode getConfig() {
        return config;
    }
}

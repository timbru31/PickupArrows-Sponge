package de.dustplanet.pickuparrows;

import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.projectile.Arrow;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.entity.projectile.source.BlockProjectileSource;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.ProjectileLaunchEvent;

/**
 * PickupArrows for Sponge.
 * Handles activities (ProjectileHit)!
 *
 * Refer to the sponge page:
 * tbd
 *
 * @author xGhOsTkiLLeRx
 * thanks to mushroomhostage for the original PickupArrows plugin!
 */

public class PickupArrowsListener {
    /**
     * PickupArrows instance.
     */
    private PickupArrows plugin;

    /**
     * Creates a new PickupArrowsListener instance.
     * @param instance the PickupArrows instance
     */
    public PickupArrowsListener(PickupArrows instance) {
        plugin = instance;
    }

    /**
     * Event called when a projectile lands.
     * @param event a ProjectileHitEvent
     */
    @Subscribe
    public void onProjectileHitEvent(ProjectileLaunchEvent event) {
        Projectile projectile = event.getLaunchedProjectile();
        // Arrow?
        if (projectile == null || !(projectile instanceof Arrow)) {
            return;
        }
        // Get data
        Arrow arrow = (Arrow) projectile;
        ProjectileSource shooter = projectile.getShooter();
        boolean onFire = false;
        if (arrow.getFireTicks() > 0) {
            onFire = true;
        }
        String shooterName = "unknown";
        if (shooter instanceof Player) {
            shooterName = "player";
        } else if (shooter instanceof BlockProjectileSource) {
            shooterName = ((BlockProjectileSource) shooter).getBlock().getType().getId().toLowerCase(); // TODO minecraft:stone
        } else if (shooter instanceof Living) {
            shooterName  = ((Living) shooter).getType().getId().toLowerCase(); // TODO getId --> getName (Bukkit)
        }

        // Return if arrow is creative
        if (plugin.getConfig().getNode("ignoreCreativeArrows").getBoolean(false) && getPickup(arrow) == 2) {
            return;
        }

        // First deny it & then check if we can allow it again
        setPickup(arrow, 0);

        // Check if shooterName is in config, otherwise fallback again
        if (!plugin.getConfig().getNode("pickupFrom." + shooterName).hasListChildren()) { // TODO right?
            shooterName = "unknown";
        }

        // New check for flexible configuration
        if (plugin.getConfig().getNode("pickupFrom." + shooterName + ".fire").getBoolean() && onFire) {
            if (!plugin.getConfig().getNode("usePermissions").getBoolean(false) || rangeCheck(arrow, shooterName, shooterName + ".fire")) {
                setPickup(arrow, 1);
            }
        } else if (plugin.getConfig().getNode("pickupFrom." + shooterName + ".normal").getBoolean() && !onFire) {
            if (!plugin.getConfig().getNode("usePermissions").getBoolean(false) || rangeCheck(arrow, shooterName, shooterName + ".normal")) {
                setPickup(arrow, 1);
            }
        }
    }

    /**
     * Sets whether the arrow is from a player or not.
     * @param arrow to change
     * @param i to allow pickup in creative (2), pickup for all (1) or disable pickup(0)
     */
    private void setPickup(Arrow arrow, int i) {
        //((CraftArrow) arrow).getHandle().fromPlayer = i;
    }

    /**
     * Returns the current pickup state of an arrow
     * @param arrow the arrow
     * @return 0 (disabled), 1 (enabled for all) or 2 (enabled for creative), depending on the state
     */
    private int getPickup(Arrow arrow) {
        return 0; // TODO
        //return ((CraftArrow) arrow).getHandle().fromPlayer;
    }

    /**
     * A simple range by nearby entities check.
     * @param arrow the shot arrow
     * @param shooterName the shooter name
     * @param permSuffix the shooter name with normal/fire suffix
     * @return if the pickup is allowed
     */
    private boolean rangeCheck(Arrow arrow, String shooterName, String permSuffix) {
        return true;
        // TODO
        // Get the range
        //        double r = plugin.getConfig().getDouble("pickupFrom." + shooterName + ".range" , 10.0);
        //        // Check for near entities
        //        List<Entity> nearbyEntities = arrow.getNearbyEntities(r, r, r);
        //        for (Entity nearbyEntity: nearbyEntities) {
        //            // Player found
        //            if (nearbyEntity instanceof Player) {
        //                Player player = (Player) nearbyEntity;
        //                // Check his permission
        //                return player.hasPermission("pickuparrows.allow." + permSuffix) || player.hasPermission("pickuparrows.allow.*");
        //            }
        //        }
        //return false;
    }
}

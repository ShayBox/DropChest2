package com.shaybox.dropchest2;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Material.*;

public class DropChestPlayerListener implements Listener {
	private DropChest plugin;

	DropChestPlayerListener(DropChest plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

		Block block = event.getClickedBlock();
		if (block == null) return;

		List<Material> materials = Arrays.asList(
			CHEST,
			TRAPPED_CHEST,
			BARREL,
			SHULKER_BOX,
			WHITE_SHULKER_BOX,
			ORANGE_SHULKER_BOX,
			MAGENTA_SHULKER_BOX,
			LIGHT_BLUE_SHULKER_BOX,
			YELLOW_SHULKER_BOX,
			LIME_SHULKER_BOX,
			PINK_SHULKER_BOX,
			GRAY_SHULKER_BOX,
			LIGHT_GRAY_SHULKER_BOX,
			CYAN_SHULKER_BOX,
			PURPLE_SHULKER_BOX,
			BLUE_SHULKER_BOX,
			BROWN_SHULKER_BOX,
			GREEN_SHULKER_BOX,
			RED_SHULKER_BOX,
			BLACK_SHULKER_BOX
		);
		if (!materials.contains(block.getType())) return;

		Player player = event.getPlayer();
		DropChestPlayer dPlayer = DropChestPlayer.getPlayer(player.getUniqueId());

		switch (dPlayer.getAction()) {
			case "ADD": {
				dPlayer.setAction("NONE");
				event.setCancelled(true);

				List<DropChestContainer> containers = new ArrayList<>();
				for (DropChestContainer container : plugin.getContainers())
					if (container.getLocation().equals(block.getLocation())) containers.add(container);

				if (containers.size() != 0) {
					player.sendMessage(RED + "This is already a container");
					return;
				}

				Location location = block.getLocation();
				Material material = block.getType();
				UUID uuid = player.getUniqueId();
				int radius = dPlayer.getRadius();
				boolean protect = plugin.getConfig().getBoolean("default-protect");
				plugin.getContainers().add(new DropChestContainer(location, material, uuid, radius, protect));
				plugin.saveContainersConfig();
				player.sendMessage(GREEN + "Added " + block.getType());
				break;
			}
			case "REMOVE": {
				dPlayer.setAction("NONE");
				event.setCancelled(true);

				List<DropChestContainer> containers = new ArrayList<>();
				for (DropChestContainer container : plugin.getContainers())
					if (container.getLocation().equals(block.getLocation())) containers.add(container);

				if (containers.size() == 0) {
					player.sendMessage(RED + "This not a container");
					return;
				}

				List<DropChestContainer> containers2 = new ArrayList<>();
				for (DropChestContainer container : containers)
					if (plugin.getConfig().getBoolean("allow-protect") && container.isProtected() && !container.getOwner().equals(player.getUniqueId()))
						containers2.add(container);

				if (containers2.size() != 0) {
					player.sendMessage(RED + "The container is protected");
					return;
				}

				plugin.getContainers().removeIf(c -> c.getLocation().equals(block.getLocation()));
				plugin.saveContainersConfig();
				player.sendMessage(GREEN + "Removed " + block.getType());
				break;
			}
			case "RADIUS": {
				dPlayer.setAction("NONE");
				event.setCancelled(true);

				List<DropChestContainer> containers = new ArrayList<>();
				for (DropChestContainer container : plugin.getContainers())
					if (container.getLocation().equals(block.getLocation())) containers.add(container);

				if (containers.size() == 0) {
					player.sendMessage(RED + "This not a container");
					return;
				}

				List<DropChestContainer> containers2 = new ArrayList<>();
				for (DropChestContainer container : containers)
					if (plugin.getConfig().getBoolean("allow-protect") && container.isProtected() && !container.getOwner().equals(player.getUniqueId()))
						containers2.add(container);

				if (containers2.size() != 0) {
					player.sendMessage(RED + "The container is protected");
					return;
				}

				int radius = dPlayer.getRadius();
				for (DropChestContainer container : plugin.getContainers()) {
					if (container.getLocation().equals(block.getLocation())) container.setRadius(radius);
				}
				plugin.saveContainersConfig();
				player.sendMessage(GREEN + "Changed " + block.getType() + " radius to " + radius);
				break;
			}
			case "OWNER": {
				dPlayer.setAction("NONE");
				event.setCancelled(true);

				List<DropChestContainer> containers = new ArrayList<>();
				for (DropChestContainer container : plugin.getContainers())
					if (container.getLocation().equals(block.getLocation())) containers.add(container);

				if (containers.size() == 0) {
					player.sendMessage(RED + "This not a container");
					return;
				}

				List<DropChestContainer> containers2 = new ArrayList<>();
				for (DropChestContainer container : containers)
					if (plugin.getConfig().getBoolean("allow-protect") && container.isProtected() && !container.getOwner().equals(player.getUniqueId()))
						containers2.add(container);

				if (containers2.size() != 0) {
					player.sendMessage(RED + "The container is protected");
					return;
				}

				Player newOwner = dPlayer.getOwner();
				for (DropChestContainer container : plugin.getContainers()) {
					if (container.getLocation().equals(block.getLocation())) container.setOwner(newOwner.getUniqueId());
				}
				plugin.saveContainersConfig();
				player.sendMessage(GREEN + "Changed " + block.getType() + " owner to " + newOwner.getName());
				break;
			}
			case "INFO": {
				dPlayer.setAction("NONE");
				event.setCancelled(true);

				List<DropChestContainer> containers = new ArrayList<>();
				for (DropChestContainer container : plugin.getContainers())
					if (container.getLocation().equals(block.getLocation())) containers.add(container);

				if (containers.size() == 0) {
					player.sendMessage(RED + "This not a container");
					return;
				}

				for (DropChestContainer container : plugin.getContainers()) {
					if (!container.getLocation().equals(block.getLocation())) continue;
					player.sendMessage(new String[]{
						GOLD + "Owner: " + YELLOW + Objects.requireNonNull(Bukkit.getPlayer(container.getOwner())).getName(),
						GOLD + "Radius: " + YELLOW + container.getRadius(),
						GOLD + "Protected: " + YELLOW + container.isProtected(),
					});
				}
			}
		}
	}
}

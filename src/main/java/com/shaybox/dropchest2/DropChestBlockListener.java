package com.shaybox.dropchest2;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class DropChestBlockListener implements Listener {
	private DropChest plugin;

	DropChestBlockListener(DropChest plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		for (DropChestContainer container : plugin.getContainers()) {
			if (!container.getLocation().equals(block.getLocation())) continue;
			if (container.getOwner().equals(player.getUniqueId()) ||
				player.hasPermission("dropchest.moderator") ||
				player.isOp() || !plugin.getConfig().getBoolean("allow-protect") ||
				!container.isProtected()
			) {
				plugin.getContainers().remove(container);
				plugin.saveContainersConfig();
				player.sendMessage(GREEN + "Removed " + block.getType());
			} else {
				event.setCancelled(true);
				player.sendMessage(RED + "That is not your " + block.getType());
			}
		}
	}
}

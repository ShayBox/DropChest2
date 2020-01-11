package com.shaybox.dropchest2.listener;

import com.shaybox.dropchest2.DropChest;
import com.shaybox.dropchest2.DropChestContainer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class BlockListener implements Listener {
	private DropChest plugin;

	public BlockListener(DropChest plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		event.getChunk().load();
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

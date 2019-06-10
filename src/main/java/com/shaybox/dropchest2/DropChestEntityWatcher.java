package com.shaybox.dropchest2;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

import static org.bukkit.Bukkit.getWorlds;
import static org.bukkit.entity.EntityType.DROPPED_ITEM;

public class DropChestEntityWatcher implements Runnable {
	private DropChest plugin;

	DropChestEntityWatcher(DropChest plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (World world : getWorlds())
			for (Entity entity : world.getEntities()) {
				if (!entity.getType().equals(DROPPED_ITEM)) continue;
				Item item = (Item) entity;
				if (!plugin.getConfig().getBoolean("wait-for-pickup") || item.getPickupDelay() > 0) continue;
				for (DropChestContainer dropChestContainer : plugin.getContainers()) {
					Location location = dropChestContainer.getLocation();
					Block block = location.getBlock();
					if (block.getType() == dropChestContainer.getMaterial()) {
						Vector distance = location.toVector().add(new Vector(0.5, 0, 0.5)).subtract(item.getLocation().toVector());
						int radius = dropChestContainer.getRadius();
						if (distance.lengthSquared() < 1.0 * radius * radius + 1) {
							Container container = (Container) block.getState();
							Inventory inventory = container.getInventory();
							HashMap<Integer, ItemStack> failedItems = inventory.addItem(item.getItemStack());
							if (!failedItems.isEmpty()) continue;
							item.remove();
							break;
						}
					} else {
						plugin.getContainers().remove(dropChestContainer);
						plugin.saveContainersConfig();
					}
				}
			}
	}
}

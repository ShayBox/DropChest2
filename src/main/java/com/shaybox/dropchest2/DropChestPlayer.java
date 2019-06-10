package com.shaybox.dropchest2;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

class DropChestPlayer {
	private static HashMap<UUID, DropChestPlayer> players = new HashMap<>();
	private String action = "NONE";
	private Player owner;
	private int radius;
	private boolean protect;

	static DropChestPlayer getPlayer(UUID uuid) {
		if (players.containsKey(uuid)) return players.get(uuid);
		else {
			DropChestPlayer dPlayer = new DropChestPlayer();
			players.put(uuid, dPlayer);
			return dPlayer;
		}
	}

	String getAction() {
		return action;
	}

	void setAction(String action) {
		this.action = action;
	}

	Player getOwner() {
		return owner;
	}

	void setOwner(Player owner) {
		this.owner = owner;
	}

	int getRadius() {
		return radius;
	}

	void setRadius(int radius) {
		this.radius = radius;
	}

	boolean isProtect() {
		return protect;
	}

	void setProtect(boolean protect) {
		this.protect = protect;
	}
}
package com.shaybox.dropchest2;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class DropChestPlayer {
	private static HashMap<UUID, DropChestPlayer> players = new HashMap<>();
	private String action = "NONE";
	private Player owner;
	private int radius;
	private boolean protect;

	public static DropChestPlayer getPlayer(UUID uuid) {
		if (players.containsKey(uuid)) return players.get(uuid);
		else {
			DropChestPlayer dPlayer = new DropChestPlayer();
			players.put(uuid, dPlayer);
			return dPlayer;
		}
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public boolean isProtect() {
		return protect;
	}

	public void setProtect(boolean protect) {
		this.protect = protect;
	}
}
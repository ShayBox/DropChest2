package com.shaybox.dropchest2;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DropChestContainer implements ConfigurationSerializable {
	private Location location;
	private Material material;
	private UUID owner;
	private int radius;
	private boolean protect;

	public DropChestContainer(Location location, Material material, UUID owner, int radius, boolean protect) {
		this.location = location;
		this.material = material;
		this.owner = owner;
		this.radius = radius;
		this.protect = protect;
	}

	public DropChestContainer(Map<String, Object> map) {
		this.location = (Location) map.get("location");
		this.material = Material.valueOf((String) map.get("material"));
		this.owner = UUID.fromString((String) map.get("owner"));
		this.radius = (int) map.get("radius");
		this.protect = (boolean) map.get("protect");
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<>();

		map.put("location", this.location);
		map.put("material", this.material.toString());
		map.put("owner", this.owner.toString());
		map.put("radius", this.radius);
		map.put("protect", this.protect);

		return map;
	}

	public Location getLocation() {
		return location;
	}

	public Material getMaterial() {
		return material;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public boolean isProtected() {
		return protect;
	}

	public void setProtect(boolean protect) {
		this.protect = protect;
	}
}
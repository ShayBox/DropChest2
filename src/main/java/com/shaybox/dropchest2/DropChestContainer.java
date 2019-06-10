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

	DropChestContainer(Location location, Material material, UUID owner, int radius, boolean protect) {
		this.location = location;
		this.material = material;
		this.owner = owner;
		this.radius = radius;
		this.protect = protect;
	}

	public DropChestContainer(Map<String, Object> serializedMedicalNote) {
		this.location = (Location) serializedMedicalNote.get("location");
		this.material = Material.valueOf((String) serializedMedicalNote.get("material"));
		this.owner = UUID.fromString((String) serializedMedicalNote.get("owner"));
		this.radius = (int) serializedMedicalNote.get("radius");
		this.protect = (boolean) serializedMedicalNote.get("protect");
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> mapSerializer = new HashMap<>();

		mapSerializer.put("location", this.location);
		mapSerializer.put("material", this.material.toString());
		mapSerializer.put("owner", this.owner.toString());
		mapSerializer.put("radius", this.radius);
		mapSerializer.put("protect", this.protect);

		return mapSerializer;
	}

	Location getLocation() {
		return location;
	}

	Material getMaterial() {
		return material;
	}

	UUID getOwner() {
		return owner;
	}

	void setOwner(UUID owner) {
		this.owner = owner;
	}

	int getRadius() {
		return radius;
	}

	void setRadius(int radius) {
		this.radius = radius;
	}

	boolean isProtected() {
		return protect;
	}

	void setProtect(boolean protect) {
		this.protect = protect;
	}
}
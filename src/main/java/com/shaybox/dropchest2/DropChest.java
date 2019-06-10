package com.shaybox.dropchest2;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;

public final class DropChest extends JavaPlugin {
	private File containersConfigFile = new File(getDataFolder(), "containers.yml");
	private FileConfiguration containersConfig = new YamlConfiguration();

	@Override
	public void onEnable() {
		// Load config class
		ConfigurationSerialization.registerClass(DropChestContainer.class);

		// Load configs
		saveDefaultConfig();
		saveDefaultContainersConfig();
		loadContainersConfig();

		// Load command
		PluginCommand command = getCommand("dropchest");
		if (command != null) command.setExecutor(new DropChestCommand(this));

		// Load task
		getScheduler().scheduleSyncRepeatingTask(this, new DropChestEntityWatcher(this), 0, getConfig().getInt("poll-interval"));

		// Load listeners
		getPluginManager().registerEvents(new DropChestPlayerListener(this), this);
		getPluginManager().registerEvents(new DropChestBlockListener(this), this);
	}

	private void saveDefaultContainersConfig() {
		if (!containersConfigFile.exists()) saveResource("containers.yml", false);
	}

	private void loadContainersConfig() {
		try {
			containersConfig.load(containersConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	void saveContainersConfig() {
		try {
			containersConfig.save(containersConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	List<DropChestContainer> getContainers() {
		return (List<DropChestContainer>) containersConfig.getList("containers");
	}
}

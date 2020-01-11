package com.shaybox.dropchest2;

import com.shaybox.dropchest2.listener.BlockListener;
import com.shaybox.dropchest2.listener.PlayerListener;
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
		initConfigs();

		PluginCommand command = getCommand("dropchest");
		if (command != null) command.setExecutor(new DropChestCommand(this));

		getScheduler().scheduleSyncRepeatingTask(this, new EntityWatcher(this), 0, getConfig().getInt("poll-interval"));

		getPluginManager().registerEvents(new BlockListener(this), this);
		getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	private void initConfigs() {
		ConfigurationSerialization.registerClass(DropChestContainer.class);

		saveDefaultConfig();
		saveDefaultContainersConfig();
	}

	private void saveDefaultContainersConfig() {
		if (!containersConfigFile.exists()) saveResource("containers.yml", false);

		try {
			containersConfig.load(containersConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void saveContainersConfig() {
		try {
			containersConfig.save(containersConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<DropChestContainer> getContainers() {
		return (List<DropChestContainer>) containersConfig.getList("containers");
	}
}

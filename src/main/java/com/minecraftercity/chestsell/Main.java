package com.minecraftercity.chestsell;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener
{
    //TODO: Write function so that if a player places a chest close to
    //TODO: A spawner he gets a message he can use the plugin.

    private File customConfigFile;
    private FileConfiguration customConfig;
    private static Economy econ = null;

    @Override
    public void onEnable()
    {
        if (!setupEconomy() ) {
            getServer().getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.saveDefaultConfig();
        this.createCustomConfig();
        Objects.requireNonNull(this.getCommand("autosell"))
                .setExecutor(new RegisterChestCommand(this));
        this.process();
    }

    private boolean setupEconomy()
    {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy()
    {
        return econ;
    }

    private void createCustomConfig()
    {
        customConfigFile = new File(getDataFolder(), "prices.yml");
        if (!customConfigFile.exists()) {
            saveResource("prices.yml", false);
        }
        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void process()
    {
        this.getServer()
                .getScheduler()
                .runTaskTimerAsynchronously(this, new RunnableBalancer(this), 0, 500);
    }

    public FileConfiguration getCustomConfig() throws IOException, InvalidConfigurationException
    {
        File file = new File(getDataFolder(), "prices.yml");
        customConfig = new YamlConfiguration();
        customConfig.load(file);
        return customConfig;
    }

}

package cc.azuramc.azurachat;

import cc.azuramc.azurachat.command.ReloadCommand;
import cc.azuramc.azurachat.listener.ChatListener;
import cc.azuramc.azurachat.manager.ConfigManager;
import cc.azuramc.azurachat.util.Metrics;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author An5w1r@163.com
 */
public final class AzuraChat extends JavaPlugin {

    @Getter
    private static AzuraChat instance;

    @Getter
    private boolean isPlaceholderApiAvailable;

    @Getter
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        // check PlaceholderAPI availability
        checkPlaceholderAPI();

        // initialize configuration manager
        this.configManager = new ConfigManager(this);
        configManager.loadConfig();

        // register event listeners
        registerListeners();

        // register commands
        registerCommands();

        // load bStats metrics
        loadMetrics();

        // log plugin enable message
        getLogger().info("AzuraChat is enabling...");
        getLogger().info("PlaceholderAPI support: " + (isPlaceholderApiAvailable ? "enabled" : "disabled"));
    }

    @Override
    public void onDisable() {
        getLogger().info("AzuraChat is disabling...");
    }

    /**
     * Check if PlaceholderAPI is available
     */
    private void checkPlaceholderAPI() {
        this.isPlaceholderApiAvailable = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (isPlaceholderApiAvailable) {
            Bukkit.getLogger().info("[AzuraChat] Detected PlaceholderAPI, enabling support.");
        } else {
            Bukkit.getLogger().warning("[AzuraChat] PlaceholderAPI not found! Some features may not work.");
        }
    }

    /**
     * Register event listeners
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    /**
     * Register commands
     */
    private void registerCommands() {
        ReloadCommand reloadCommand = new ReloadCommand(this);
        getCommand("azurachat").setExecutor(reloadCommand);
        getCommand("azurachat").setTabCompleter(reloadCommand);
    }

    /**
     * Load bStats metrics
     */
    private void loadMetrics() {
        new Metrics(this, 26890);
    }
}

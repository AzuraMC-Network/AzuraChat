package cc.azuramc.azurachat.manager;

import cc.azuramc.azurachat.AzuraChat;
import cc.azuramc.azurachat.util.ChatColorUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuration Manager for AzuraChat
 * Handles loading and accessing configuration values
 *
 * @author an5w1r@163.com
 */
public class ConfigManager {

    private final AzuraChat plugin;
    private FileConfiguration config;

    @Getter private String defaultFormat;
    @Getter private boolean permissionFormatsEnabled;
    @Getter private Map<String, String> permissionFormats;

    /**
     * Constructor for ConfigManager
     *
     * @param plugin The plugin instance
     */
    public ConfigManager(AzuraChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Load configuration from file
     */
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        // Load chat format settings
        this.defaultFormat = ChatColorUtil.color(config.getString("chat.default-format", "&7%player_name%&f: &7%message%"));

        // Load permission formats enabled state
        this.permissionFormatsEnabled = config.getBoolean("chat.permission-formats.enabled", false);

        // Load permission formats in order to maintain priority
        this.permissionFormats = new LinkedHashMap<>();

        // Get the permission-formats section and load all keys in order
        ConfigurationSection permissionSection = config.getConfigurationSection("chat.permission-formats");
        if (permissionSection != null) {
            // Use getKeys(true) to get full keys including nested paths
            for (String key : permissionSection.getKeys(true)) {
                Object value = permissionSection.get(key);
                // Only add if the value is a String, not a ConfigurationSection
                if (value instanceof String) {
                    String format = (String) value;
                    this.permissionFormats.put(key, format);
                    plugin.getLogger().info("Loaded permission format: " + key + " -> " + format);
                }
            }
        }

        plugin.getLogger().info("Total permission formats loaded: " + this.permissionFormats.size());
    }

    /**
     * Get chat format for a specific player based on their permissions
     * Checks permissions in the order they appear in the configuration file
     *
     * @param player The player to get format for
     * @return The chat format string
     */
    public String getChatFormat(Player player) {
        // Check if permission formats are enabled
        if (!permissionFormatsEnabled) {
            return defaultFormat;
        }

        // Check for permission-specific formats in order
        for (String permission : permissionFormats.keySet()) {
            if (player.hasPermission(permission)) {
                return ChatColorUtil.color(permissionFormats.get(permission));
            }
        }

        // Return default format if no permission matches
        return defaultFormat;
    }

    /**
     * Reload configuration from file
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
        plugin.getLogger().info("Configuration reloaded successfully.");
    }
}

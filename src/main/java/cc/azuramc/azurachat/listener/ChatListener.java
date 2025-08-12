package cc.azuramc.azurachat.listener;

import cc.azuramc.azurachat.AzuraChat;
import cc.azuramc.azurachat.util.ChatColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Chat event listener for AzuraChat
 * Handles chat formatting with permission-based formats and PlaceholderAPI support
 *
 * @author An5w1r@163.com
 */
public class ChatListener implements Listener {

    private final AzuraChat plugin;

    public ChatListener(AzuraChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        String message = event.getMessage();

        event.setFormat(ChatColorUtil.parse(player,
                plugin.getConfigManager().getChatFormat(player)
                        .replace("%player_name%", player.getName())
                        .replace("%message%", message)
                        .replace("%player_displayname%", player.getDisplayName())));
    }
}

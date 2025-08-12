package cc.azuramc.azurachat.command;

import cc.azuramc.azurachat.AzuraChat;
import cc.azuramc.azurachat.util.ChatColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Command handler for AzuraChat commands
 *
 * @author an5w1r@163.com
 */
public class ReloadCommand implements CommandExecutor, TabCompleter {

    private final AzuraChat plugin;

    public ReloadCommand(AzuraChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("azurachat.admin")) {
            sender.sendMessage(ChatColorUtil.color("&cYou have no permission to use this command!"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColorUtil.color("&b&lAzuraChat &8- &7v" + plugin.getDescription().getVersion() + " &8Command Help"));
            sender.sendMessage("");
            sender.sendMessage(ChatColorUtil.color("&7 • &f/azurachat reload &7- Reload the configuration"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("azurachat.reload")) {
                sender.sendMessage(ChatColorUtil.color("&cYou do not have permission to reload the configuration!"));
                return true;
            }

            try {
                plugin.getConfigManager().reloadConfig();
                sender.sendMessage(ChatColorUtil.color("&aConfiguration reloaded successfully!"));
            } catch (Exception e) {
                sender.sendMessage(ChatColorUtil.color("&cReload configuration error: " + e.getMessage()));
                plugin.getLogger().severe("Reload configuration error: " + e.getMessage());
                e.printStackTrace();
            }
            return true;
        }

        sender.sendMessage(ChatColorUtil.color("&cUnknow command! Use /azurachat for help."));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("azurachat.admin")) {
                completions.add("reload");
            }
        }

        return completions;
    }
}

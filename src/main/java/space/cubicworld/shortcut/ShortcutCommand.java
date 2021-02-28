package space.cubicworld.shortcut;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.command.DiscomcAdminCommand;

import java.util.Collections;
import java.util.List;

public class ShortcutCommand extends DiscomcAdminCommand {

    public ShortcutCommand() {
        super("make shortcut using this command", "discomc.shortcut");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length >= 3){
            String shortcutName = args[1];
            StringBuilder shortcutContentBuilder = new StringBuilder();
            for (int i = 2; i < args.length; ++i){
                shortcutContentBuilder.append(args[i]);
                if (i != args.length - 1){
                    shortcutContentBuilder.append(" ");
                }
            }
            String shortcutContent = shortcutContentBuilder.toString();
            DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
            ShortcutModule shortcutModule = discomcPlugin.getShortcutModule();
            shortcutModule.getShortcuts().put(shortcutName.toLowerCase(), shortcutContent);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length <= 2) return Collections.singletonList("<shortcut name>");
        else return Collections.singletonList("<shortcut content>");
    }
}

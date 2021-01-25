package com.github.jenya705.command;

import com.github.jenya705.command.admin.ReloadAdminCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;

import java.util.*;

public class DiscomcCommand implements CommandExecutor, TabExecutor {

    private Map<String, AdminCommand> adminCommands = new HashMap<>();

    public DiscomcCommand(){
        adminCommands.put("reload", new ReloadAdminCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0){
            writeHelp(commandSender);
        }
        else if (args.length >= 1){
            AdminCommand adminCommand = adminCommands.getOrDefault(args[0], null);
            if (adminCommand == null){
                writeHelp(commandSender);
            }
            else {
                if (!commandSender.hasPermission(adminCommand.getPermission())){
                    DiscomcMessages discomcMessages = DiscomcPlugin.getInstance().getDiscomcMessages();
                    commandSender.sendMessage(discomcMessages.getMessage(discomcMessages.getNotAllowedCommand(), true));
                    writeHelp(commandSender);
                    return true;
                }
                adminCommand.onCommand(commandSender, command, label, args);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return new ArrayList<String>(adminCommands.keySet());
        }
        return null;
    }

    public void writeHelp(CommandSender commandSender){
        commandSender.sendMessage("§bDisco§amc§r help");
        for (Map.Entry<String, AdminCommand> entry: adminCommands.entrySet()){
            AdminCommand adminCommand = entry.getValue();
            commandSender.sendMessage(
                    String.format("%s%s §r- %s", ChatColor.GOLD, adminCommand.getName(), adminCommand.getHelp())
            );
        }
    }

}

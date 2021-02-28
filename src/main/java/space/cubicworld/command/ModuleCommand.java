package space.cubicworld.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcModule;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.ModuleStore;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

public class ModuleCommand extends DiscomcAdminCommand {

    public ModuleCommand() {
        super("see module", "discomc.module");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        if (args.length == 1){
            return false;
        }
        String moduleName = args[1];
        DiscomcModule discomcModule = ModuleStore.getModule(moduleName);
        if (args.length == 2){
            infoModuleCommand(commandSender, moduleName, discomcModule);
        }
        else {
            String commandName = args[2];
            if (commandName.equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("discomc.module.reload")) {
                    reloadModuleCommand(commandSender, discomcModule);
                } else {
                    commandSender.sendMessage(discomcMessages.getCommandNotPermission());
                }
            } else if (commandName.equalsIgnoreCase("info")) {
                if (commandSender.hasPermission("discomc.module.info")) {
                    infoModuleCommand(commandSender, moduleName, discomcModule);
                } else {
                    commandSender.sendMessage(discomcMessages.getCommandNotPermission());
                }
            } else {
                commandSender.sendMessage(MessageFormat.format(
                        discomcMessages.getCommandNotExist(), commandName
                ));
            }

        }

        return true;

    }

    private void reloadModuleCommand(CommandSender commandSender, DiscomcModule discomcModule){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        try {
            discomcModule.load();
            commandSender.sendMessage(discomcMessages.getModuleReloadSuccess());
        } catch (Exception e){
            discomcModule.setEnabled(false);
            commandSender.sendMessage(discomcMessages.getModuleReloadFailed());
            discomcPlugin.getLogger().log(Level.WARNING, "Reload failed:", e);
        }
    }

    private void infoModuleCommand(CommandSender commandSender, String name, DiscomcModule discomcModule){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        commandSender.sendMessage(MessageFormat.format(
                discomcMessages.getModuleInfo(), name,
                discomcModule.isEnabled() ? ChatColor.GREEN : ChatColor.RED,
                discomcModule.getDescription()
        ));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 2){
            List<String> tab = new ArrayList<>();
            ModuleStore.getModules().forEach((name, module) -> tab.add(name));
            return tab;
        }
        else {
            return Arrays.asList(
                    "reload", "info"
            );
        }
    }
}

package space.cubicworld.command;

import javafx.util.Pair;
import lombok.Getter;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.connect.ConnectForceCommand;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

public class DiscomcCommand implements CommandExecutor, TabExecutor {

    public static final String DISCOMC_COMMAND = "discomc";

    @Getter
    private List<Pair<String, DiscomcAdminCommand>> commands;

    public DiscomcCommand(){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        PluginCommand command = discomcPlugin.getCommand(DISCOMC_COMMAND);
        if (command == null) {
            discomcPlugin.getLogger().log(Level.SEVERE, "Can not load discomc command! Disabling plugin!");
            DiscomcPlugin.pluginEnabled(false);
            return;
        }
        commands = new ArrayList<>();
        putCommand("help", new HelpCommand());
        putCommand("reload", new ReloadCommand());
        putCommand("connectForce", new ConnectForceCommand());
        putCommand("userGet", new UserGetCommand());
        putCommand("module", new ModuleCommand());
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String commandName;
        if (args.length == 0) commandName = "help";
        else commandName = args[0];
        DiscomcAdminCommand adminCommand = getCommand(commandName);
        DiscomcMessages discomcMessages = DiscomcPlugin.getDiscomcPlugin().getDiscomcMessages();
        if (adminCommand == null){
            commandSender.sendMessage(MessageFormat.format(
                    discomcMessages.getCommandNotExist(), commandName
            ));
        }
        else if (!commandSender.hasPermission(adminCommand.getPermission())){
            commandSender.sendMessage(MessageFormat.format(
                    discomcMessages.getCommandNotPermission(), adminCommand.getPermission()
            ));
        }
        else {
            return adminCommand.onCommand(commandSender, command, label, args);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            List<String> names = new ArrayList<>();
            commands.forEach(it -> names.add(it.getKey()));
            return names;
        } else {
            DiscomcAdminCommand discomcAdminCommand = getCommand(args[0]);
            if (discomcAdminCommand == null) return Collections.singletonList("<Command is not exist>");
            return discomcAdminCommand.onTabComplete(commandSender, command, label, args);
        }
    }

    public DiscomcAdminCommand getCommand(String command){
        for (Pair<String, DiscomcAdminCommand> pair: commands){
            if (pair.getKey().equalsIgnoreCase(command)){
                return pair.getValue();
            }
        }
        return null;
    }

    public void putCommand(String name, DiscomcAdminCommand command){
        commands.add(new Pair<>(name, command));
    }

}

package space.cubicworld.command.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.command.AdminCommand;


public class ReloadAdminCommand extends AdminCommand {
    public ReloadAdminCommand() {
        super("reload", "reload all discomc's configs", "discomc.reload");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        discomcPlugin.finishAll();
        discomcPlugin.loadAll();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        commandSender.sendMessage(discomcMessages.getMessage(discomcMessages.getReloaded(), true));
        return true;
    }
}

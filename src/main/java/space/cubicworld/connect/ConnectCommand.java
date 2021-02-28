package space.cubicworld.connect;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.database.DatabaseModule;
import space.cubicworld.discord.DiscordModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;

public class ConnectCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (commandSender instanceof Player){
            DiscomcPlugin.getDiscomcPlugin().getServer().getScheduler().runTaskAsynchronously(DiscomcPlugin.getDiscomcPlugin(), () -> {
                Player player = (Player) commandSender;
                DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
                ConnectModule connectModule = discomcPlugin.getConnectModule();
                DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
                DiscomcSave discomcSave = discomcPlugin.getDiscomcSave();
                DiscordModule discordModule = discomcPlugin.getDiscordModule();
                DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
                String connectChannelName = discordModule.getJda().
                        getTextChannelById(discomcSave.getConnectChannelID()).getName();
                Long userID = ConnectModule.getDiscordId(player.getUniqueId());
                if (userID == null){
                    for (Map.Entry<Integer, Player> entry : connectModule.getCodes().entrySet()){
                        if (entry.getValue().equals(player)){
                            player.sendMessage(MessageFormat.format(
                                    discomcMessages.getConnectRequestMinecraft(), Integer.toString(entry.getKey()), connectChannelName
                            ));
                            return;
                        }
                    }
                    ConnectConfiguration connectConfiguration = connectModule.getConfig();
                    int code;
                    do {
                        code = DiscomcPlugin.random.nextInt(connectConfiguration.getMaxCodeValue());
                    } while (connectModule.getCodes().containsKey(code));
                    connectModule.getCodes().put(code, player);
                    player.sendMessage(MessageFormat.format(
                            discomcMessages.getConnectRequestMinecraft(), Integer.toString(code), connectChannelName
                    ));
                    int finalCode = code;
                    if (connectConfiguration.getCodeRemovingTime() != -1) {
                        discomcPlugin.getServer().getScheduler().runTaskLaterAsynchronously(discomcPlugin, () -> {
                            if (connectModule.getCodes().remove(finalCode) != null) {
                                player.sendMessage(discomcMessages.getConnectTimeEndMinecraft());
                            }
                        }, connectConfiguration.getCodeRemovingTime());
                    }
                }
                else if (userID == -1){
                    player.sendMessage(discomcMessages.getSqlExceptionMinecraft());
                }
                else {
                    User user = discordModule.getJda().retrieveUserById(userID).complete();
                    if (user != null) {
                        player.sendMessage(MessageFormat.format(
                                discomcMessages.getConnectAlreadyMinecraft(), user.getAsTag()
                        ));
                    }
                }
            });
            return true;
        }
        else {
            commandSender.sendMessage("This command only for humans!");
        }
        return false;
    }
}

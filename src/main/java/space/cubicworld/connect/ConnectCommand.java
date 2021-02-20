package space.cubicworld.connect;

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
                try {
                    ResultSet result = databaseModule.query("SELECT * FROM dmc_players WHERE uuidMost = ? AND uuidLeast = ?",
                            player.getUniqueId().getMostSignificantBits(), player.getUniqueId().getLeastSignificantBits());
                    if (result.next()){
                        long userID = result.getLong("discordID");
                        player.sendMessage(MessageFormat.format(
                                discomcMessages.getConnectAlreadyMinecraft(), discordModule.getJda()
                                    .retrieveUserById(userID).complete().getAsTag()
                        ));
                        return;
                    }
                } catch (SQLException e){
                    player.sendMessage(discomcMessages.getSqlExceptionMinecraft());
                    discomcPlugin.getLogger().log(Level.SEVERE, "Exception while trying to check for player linked:", e);
                    return;
                }
                for (Map.Entry<Integer, Player> entry : connectModule.getCodes().entrySet()){
                    if (entry.getValue().equals(player)){
                        player.sendMessage(MessageFormat.format(
                            discomcMessages.getConnectRequestMinecraft(), entry.getKey(), connectChannelName
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
                discomcPlugin.getServer().getScheduler().runTaskLaterAsynchronously(discomcPlugin, () -> {
                    connectModule.getCodes().remove(finalCode);
                    player.sendMessage(discomcMessages.getConnectTimeEndMinecraft());
                }, connectConfiguration.getCodeRemovingTime());
            });
            return true;
        }
        else {
            commandSender.sendMessage("This command only for humans!");
        }
        return false;
    }
}

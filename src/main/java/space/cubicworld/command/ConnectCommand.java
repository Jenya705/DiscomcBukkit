package com.github.jenya705.command;

import ch.jalu.configme.SettingsManager;
import space.cubicworld.DiscomcPlugin;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.*;
import space.cubicworld.service.ConnectTrashRemover;
import space.cubicworld.DiscomcDatabase;
import space.cubicworld.connect.DiscordConnectHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ConnectCommand implements CommandExecutor {

    private static List<UUID> alreadyWrote = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only player can write this command!");
            return true;
        }
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        try {
            DiscordManager discordManager = discomcPlugin.getDiscordManager();
            DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
            DiscomcDatabase discomcDatabase = discomcPlugin.getDiscomcDatabase();
            SettingsManager discomcConfiguration = discomcPlugin.getDiscomcConfig();

            if (!discomcConfiguration.getProperty(DiscomcConfiguration.CONNECT_ENABLED)){
                commandSender.sendMessage(discomcMessages.getMessage(discomcMessages.getConnectDisabled(), true));
                return true;
            }

            Player player = (Player) commandSender;

            if (alreadyWrote.contains(player.getUniqueId())){
                player.sendMessage(discomcMessages.getMessage(discomcMessages.getAlreadyWroteConnect(), true));
                return true;
            }

            ResultSet resultSet = discomcDatabase.query("SELECT * FROM dmc_players WHERE mostUUID = ? AND lessUUID = ?",
                    player.getUniqueId().getMostSignificantBits(), player.getUniqueId().getLeastSignificantBits());
            boolean haveNext = resultSet.next();

            if (haveNext){
                commandSender.sendMessage(discomcMessages.getMessage(discomcMessages.getAlreadyConnected(), true));
                return true;
            }

            TextChannel connectChannel = discordManager.getJda().getTextChannelById(
                    discomcPlugin.getDiscomcSave().getProperty(DiscomcSave.CONNECT_CHANNEL_ID));
            short code = DiscordConnectHandler.addCode(player.getUniqueId());
            commandSender.sendMessage(discomcMessages.getMessage(discomcMessages.getConnectMessage(), true, Short.toString(code), connectChannel.getName()));

            DiscomcPlugin.addBukkitTask(new ConnectTrashRemover(code).runTaskLaterAsynchronously(discomcPlugin,
                    discomcConfiguration.getProperty(DiscomcConfiguration.CODE_REMOVE_SECONDS)));

        } catch (SQLException e){
            commandSender.sendMessage("External error, write administrators about this, error wrote in console");
            discomcPlugin.getLogger().log(Level.SEVERE, "Error while executing connect command:", e);
        }

        return true;
    }

    public static void removeAlreadyWrotePlayer(UUID uuid){
        alreadyWrote.remove(uuid);
    }

}

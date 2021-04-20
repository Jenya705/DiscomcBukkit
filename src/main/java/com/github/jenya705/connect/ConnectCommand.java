package com.github.jenya705.connect;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcDatabaseUser;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.util.UsersUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.TextChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Set;

public class ConnectCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Discomc discomc = Discomc.getPlugin();
        ConnectModule connectModule = discomc.getConnectModule();
        DiscordModule discordModule = discomc.getDiscordModule();
        Player player = (Player) commandSender;
        discomc.getScheduler().runTaskAsynchronously(() -> {
            DiscomcDatabaseUser playerConnectedAccount = UsersUtil.getUser(player.getUniqueId());
            if (playerConnectedAccount != null) {
                User user = discordModule.getClient().getUserById(
                        Snowflake.of(playerConnectedAccount.getDiscordID())).block();
                if (user != null) {
                    player.sendMessage(MessageFormat.format(
                            connectModule.getConfig().getAlreadyConnected(),
                            user.getUsername() + "#" + user.getDiscriminator()));
                } else {
                    player.sendMessage(MessageFormat.format(
                            connectModule.getConfig().getAlreadyConnected(),
                            ""));
                }
                return;
            }
            int code = connectModule.registerCode(player.getUniqueId());
            TextChannel connectChannel = (TextChannel) discordModule.getClient()
                    .getChannelById(Snowflake.of(connectModule.getConfig().getConnectChannelID()))
                    .filter(it -> it.getType() == Channel.Type.GUILD_TEXT)
                    .block();
            String connectChannelName;
            if (connectChannel != null) connectChannelName = connectChannel.getName();
            else connectChannelName = "INVALID";
            player.sendMessage(MessageFormat.format(
                    connectModule.getConfig().getConnectRequest(),
                    Integer.toString(code), connectChannelName));
        });
        return true;
    }
}

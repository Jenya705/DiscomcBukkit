package space.cubicworld.connect;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcUtil;
import space.cubicworld.command.DiscomcAdminCommand;
import space.cubicworld.database.DatabaseModule;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ConnectForceCommand extends DiscomcAdminCommand {

    public ConnectForceCommand() {
        super("force connection with minecraft and discord accounts", "discomc.forceConnect");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length >= 3){
            DiscomcPlugin.getDiscomcPlugin().getServer().getScheduler().runTaskAsynchronously(DiscomcPlugin.getDiscomcPlugin(), () -> {
                String minecraftNickname = args[1];
                String discordNickname = args[2];
                String discordIdNotParsed;
                long discordId;
                DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
                DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
                if (discordNickname.startsWith("<@!") && discordNickname.endsWith(">")) {
                    discordIdNotParsed = discordNickname.substring(3, discordNickname.length()-1);
                }
                else {
                    discordIdNotParsed = discordNickname;
                }
                try {
                    discordId = Long.parseLong(discordIdNotParsed);
                } catch (NumberFormatException e){
                    commandSender.sendMessage(MessageFormat.format(
                            discomcMessages.getConnectForceIDIsNotNumber(), discordIdNotParsed));
                    return;
                }
                DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
                UUID playerUUID;
                playerUUID = DiscomcUtil.getUuidPlayer(minecraftNickname);
                if (playerUUID == null){
                    commandSender.sendMessage(MessageFormat.format(
                            discomcMessages.getConnectForcePlayerIsNotExists(), minecraftNickname
                    ));
                    return;
                }
                boolean success = ConnectModule.upsertPlayer(playerUUID, discordId);
                if (success) {
                    DiscomcUtil discomcUtil = discomcPlugin.getDiscomcUtil();
                    if (discomcUtil.getCachedPlayers().containsKey(discordId)) {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
                        discomcUtil.getCachedPlayers().put(discordId, player);
                    }
                    commandSender.sendMessage(MessageFormat.format(
                            discomcMessages.getConnectForceSuccess(), minecraftNickname, discordIdNotParsed
                    ));
                }
                else {
                    commandSender.sendMessage(discomcMessages.getSqlExceptionMinecraft());
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 2) return Collections.singletonList("<minecraft nickname>");
        else if (args.length == 3) return Arrays.asList("<mention>", "<discord id>");
        return null;
    }
}

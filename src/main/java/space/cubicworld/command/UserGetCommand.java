package space.cubicworld.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcUtil;
import space.cubicworld.database.DatabaseModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

public class UserGetCommand extends DiscomcAdminCommand{

    public static final int MAX_COUNT = 7;

    public UserGetCommand() {
        super("get user info", "discomc.userGet");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length >= 2){
            DiscomcPlugin.getDiscomcPlugin().getServer().getScheduler().runTaskAsynchronously(DiscomcPlugin.getDiscomcPlugin(), () -> {
                DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
                DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
                String identifier = args[1];
                try {
                    if (identifier.startsWith("<@!") && identifier.endsWith(">")) {
                        String discordIdNotParsed = identifier.substring(3, identifier.length() - 1);
                        long discordId;
                        discordId = Long.parseLong(discordIdNotParsed);
                        writeResultsForDiscordId(commandSender, discordId);
                    } else if (identifier.contains(":")) {
                        if (identifier.startsWith("minecraft:")) {
                            writeResultsForMinecraftNickname(commandSender, identifier.substring(10));
                        } else if (identifier.startsWith("discord:")) {
                            writeResultsForDiscordId(commandSender, Long.parseLong(identifier.substring(8)));
                        } else {
                            commandSender.sendMessage(MessageFormat.format(
                                    discomcMessages.getUserGetInvalidIdentifier(), identifier
                            ));
                        }
                    } else {
                        writeResultsForMinecraftNickname(commandSender, identifier);
                    }
                } catch (SQLException sqlException){
                    commandSender.sendMessage(discomcMessages.getSqlExceptionMinecraft());
                    discomcPlugin.getLogger().log(Level.SEVERE, String.format("SQLException while invoking userGet command, identifier: %s:",
                            identifier), sqlException);
                } catch (NumberFormatException invalidException){
                    commandSender.sendMessage(MessageFormat.format(
                            discomcMessages.getUserGetInvalidIdentifier(), identifier));
                }
            });
        }

        return false;
    }

    protected void writeResultsForDiscordId(CommandSender commandSender, long discordId) throws SQLException {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
        ResultSet result = databaseModule.query(databaseModule.getScriptStore()
                .getPlayerSelectByDiscordIDScript(), discordId);
        Set<UUID> uuidsSet = new HashSet<>();
        while (result.next() && uuidsSet.size() < MAX_COUNT){
            uuidsSet.add(new UUID(result.getLong("uuidMost"), result.getLong("uuidLeast")));
        }
        TextComponent textComponent = new TextComponent(MessageFormat.format(
                discomcMessages.getUserGetAccountsCount(), uuidsSet.size()
        ) + "\n");
        for (UUID uuid: uuidsSet){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            String nicknameShow;
            if (offlinePlayer.getName() == null) nicknameShow = uuid.toString();
            else nicknameShow = offlinePlayer.getName();
            TextComponent uuidTextComponent = new TextComponent(MessageFormat.format(
                    discomcMessages.getUserGetMinecraftNicknameMessage(), nicknameShow
            ) + "\n");
            uuidTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, nicknameShow));
            textComponent.addExtra(uuidTextComponent);
        }
        textComponent.addExtra(new TextComponent(discomcMessages.getUserGetFooter()));
        commandSender.spigot().sendMessage(textComponent);
    }

    protected void writeResultsForMinecraftNickname(CommandSender commandSender, String nickname) throws SQLException{
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
        UUID playerUUID = DiscomcUtil.getUuidPlayer(nickname);
        if (playerUUID != null) {
            ResultSet result = databaseModule.query(databaseModule.getScriptStore().getPlayerSelectByUuidScript(),
                    playerUUID.getMostSignificantBits(), playerUUID.getLeastSignificantBits());
            Set<Long> discordIds = new HashSet<>();
            while (result.next() && discordIds.size() < MAX_COUNT){
                discordIds.add(result.getLong("discordid"));
            }
            TextComponent textComponent = new TextComponent(MessageFormat.format(
                    discomcMessages.getUserGetAccountsCount(), discordIds.size()
            ) + "\n");
            for (Long discordId: discordIds){
                TextComponent discordIdTextComponent = new TextComponent(MessageFormat.format(
                        discomcMessages.getUserGetDiscordIDMessage(), Long.toString(discordId)
                ) + "\n");
                discordIdTextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Long.toString(discordId)));
                textComponent.addExtra(discordIdTextComponent);
            }
            textComponent.addExtra(new TextComponent(discomcMessages.getUserGetFooter()));
            commandSender.spigot().sendMessage(textComponent);
        }
        else {
            commandSender.sendMessage(MessageFormat.format(
                    discomcMessages.getUserGetAccountsCount(), 0
            ) + '\n' + discomcMessages.getUserGetFooter());
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return Arrays.asList("minecraft:", "discord:", "<mention>", "<minecraft nickname>");
    }
}

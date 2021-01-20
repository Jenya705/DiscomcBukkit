package space.cubicworld.discord;

import ch.jalu.configme.SettingsManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.database.DiscomcDatabase;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class DiscordConnectHandler extends ListenerAdapter {

    private static Map<Short, UUID> codes = new HashMap<>();

    private static final Random random = new Random();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();

        if (event.getMessage().getAuthor().getIdLong() == discomcPlugin.getDiscordManager().getJda().getSelfUser().getIdLong()) return;

        SettingsManager discomcSave = discomcPlugin.getDiscomcSave();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        TextChannel textChannel = event.getChannel();

        if (discomcSave.getProperty(DiscomcSave.CONNECT_CHANNEL_ID) != textChannel.getIdLong()) return;

        String messageRaw = event.getMessage().getContentRaw();
        short code;
        event.getMessage().delete().queue();
        try {
            code = Short.parseShort(messageRaw);
        } catch (NumberFormatException e){
            textChannel.sendMessage(discomcMessages.getMessage(discomcMessages.getCodeNotNumber(), false, event.getMessage().getAuthor().getAsMention()))
                    .delay(5, TimeUnit.SECONDS)
                    .queue( it -> it.delete().queue());
            return;
        }

        UUID playerUUID = codes.getOrDefault(code, null);
        if (playerUUID != null){
            DiscomcDatabase discomcDatabase = discomcPlugin.getDiscomcDatabase();
            try {
                discomcDatabase.update("INSERT INTO dmc_players (mostUUID, lessUUID, discordID) VALUES (?, ?, ?)",
                        playerUUID.getMostSignificantBits(), playerUUID.getLeastSignificantBits(), event.getMessage().getAuthor().getIdLong());
                removeCode(code);
                textChannel.sendMessage(discomcMessages.getMessage(discomcMessages.getConnectSuccess(), false, event.getMessage().getAuthor().getAsMention()))
                        .delay(5, TimeUnit.SECONDS)
                        .queue(it -> it.delete().queue());
                Player player = Bukkit.getPlayer(playerUUID);
                player.sendMessage(discomcMessages.getMessage(discomcMessages.getConnectSuccessInMinecraft(), true));
            } catch (SQLException e){
                discomcPlugin.getLogger().log(Level.SEVERE, "Error while receiving connect code:", e);
            }
        }
        else {
            textChannel.sendMessage(discomcMessages.getMessage(discomcMessages.getCodeNotExist(), false, event.getMessage().getAuthor().getAsMention()))
                    .delay(5, TimeUnit.SECONDS)
                    .queue(it -> it.delete().queue());
        }

    }

    public static short addCode(UUID playerUUID){
        while (true){
            short code = (short) (random.nextInt() % (256 * 256));
            UUID playerUUIDWithCode = codes.getOrDefault(code, null);
            if (playerUUIDWithCode == null) {
                codes.put(code, playerUUID);
                return code;
            }
        }
    }

    public static void removeCode(Short code){
        codes.remove(code);
    }

}

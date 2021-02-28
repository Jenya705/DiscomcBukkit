package space.cubicworld.connect;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.database.DatabaseModule;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ConnectDiscordHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcSave discomcSave = discomcPlugin.getDiscomcSave();
        if (event.getAuthor().isBot() || event.getChannel().getIdLong() !=
                discomcSave.getConnectChannelID()) return;
        User author = event.getAuthor();
        Message message = event.getMessage();
        ConnectModule connectModule = discomcPlugin.getConnectModule();
        ConnectConfiguration connectConfiguration = connectModule.getConfig();
        TextChannel textChannel = event.getChannel();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        message.delete().queue();
        int code;
        try {
            code = Integer.parseInt(message.getContentRaw());
        } catch (NumberFormatException e){
            MessageAction action = textChannel.sendMessage(MessageFormat.format(
                    discomcMessages.getConnectFailedDiscord(), author.getAsMention()));
            if (connectModule.getConfig().getDiscordMessageDeleteTime() != -1){
                action.delay(5, TimeUnit.SECONDS)
                        .queue(it -> it.delete().queue());
            }
            else {
                action.queue();
            }
            return;
        }
        DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
        Player player = connectModule.getCodes().getOrDefault(code, null);
        if (player != null){
            boolean success = ConnectModule.insertPlayer(player.getUniqueId(), author.getIdLong());
            if (success) {
                connectModule.getCodes().remove(code);
                MessageAction action = textChannel.sendMessage(MessageFormat.format(
                        discomcMessages.getConnectSuccessDiscord(), author.getAsMention()));
                if (connectModule.getConfig().getDiscordMessageDeleteTime() != -1) {
                    action.delay(5, TimeUnit.SECONDS)
                            .queue(it -> it.delete().queue());
                } else {
                    action.queue();
                }
                player.sendMessage(MessageFormat.format(
                        discomcMessages.getConnectSuccessMinecraft(), author.getAsTag()
                ));
            }
            else {
                MessageAction action = textChannel.sendMessage(MessageFormat.format(
                        discomcMessages.getSqlExceptionDiscord(), author.getAsMention()));
                if (connectModule.getConfig().getDiscordMessageDeleteTime() != -1){
                    action.delay(5, TimeUnit.SECONDS)
                            .queue(it -> it.delete().queue());
                }
            }
        }
        else {
            MessageAction action = textChannel.sendMessage(MessageFormat.format(
                    discomcMessages.getConnectFailedDiscord(), author.getAsMention()));
            if (connectModule.getConfig().getDiscordMessageDeleteTime() != -1){
                action.delay(5, TimeUnit.SECONDS)
                        .queue(it -> it.delete().queue());
            }
            else {
                action.queue();
            }
        }
    }
}

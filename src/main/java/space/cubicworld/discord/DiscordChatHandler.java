package space.cubicworld.discord;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcConfiguration;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.DiscordManager;

import java.text.MessageFormat;

public class DiscordChatHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        if (event.getMessage().getAuthor().isBot()) return;
        TextChannel textChannel = event.getChannel();
        if (discomcPlugin.getDiscomcSave().getProperty(DiscomcSave.CHAT_CHANNEL_ID) != textChannel.getIdLong()) return;

        String builtMessage = buildMessage(event.getAuthor().getName(), event.getMessage().getContentRaw());
        Bukkit.broadcastMessage(builtMessage); // async event

    }

    public static String buildMessage(String userName, String message){
        String messagePattern = DiscomcPlugin.getInstance().getDiscomcConfig().
                getProperty(DiscomcConfiguration.MULTI_CHAT_MESSAGE_PATTERN);
        return MessageFormat.format(messagePattern, userName, message);
    }

}

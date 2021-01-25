package space.cubicworld.multichat;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcConfiguration;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;

import java.awt.*;
import java.text.MessageFormat;

public class DiscordChatHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        if (event.getMessage().getAuthor().isBot()) return;
        TextChannel textChannel = event.getChannel();
        if (discomcPlugin.getDiscomcSave().getProperty(DiscomcSave.CHAT_CHANNEL_ID) != textChannel.getIdLong()) return;
        Color color = null;
        if (discomcPlugin.getDiscomcConfig().getProperty(DiscomcConfiguration.COLOR_NICKNAMES_WITH_HIGHEST_ROLE)) color = event.getMember().getColor();
        String builtMessage = buildMessage(color, discomcPlugin.getDiscomcConfig().getProperty(DiscomcConfiguration.FORMATTING_MESSAGES),
                event.getAuthor().getName(), event.getMessage().getContentRaw());
        Bukkit.broadcastMessage(builtMessage); // async event
    }

    public static String buildMessage(Color userColor, boolean formatting, String userName, String message){
        if (userColor != null) {
            String userColorHex = "#";
            userColorHex += Integer.toHexString(userColor.getRed());
            userColorHex += Integer.toHexString(userColor.getGreen());
            userColorHex += Integer.toHexString(userColor.getBlue());
            userName = ChatColor.of(userColorHex) + userName + ChatColor.RESET;
        }
        if (formatting){

        }
        String messagePattern = DiscomcPlugin.getInstance().getDiscomcConfig().
                getProperty(DiscomcConfiguration.MULTI_CHAT_MESSAGE_PATTERN);
        return MessageFormat.format(messagePattern, userName, message);
    }

    public static String buildMessage(Message message){
        return null;
    }

}

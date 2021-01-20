package space.cubicworld.discord;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;

public class DiscordChatHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        TextChannel textChannel = event.getChannel();
        if (discomcPlugin.getDiscomcSave().getProperty(DiscomcSave.CHAT_CHANNEL_ID) != textChannel.getIdLong()) return;

        String builtMessage = buildMessage(event.getAuthor().getName(), event.getMessage().getContentRaw());
        Bukkit.broadcastMessage(builtMessage); // async event

    }

    public String buildMessage(String userName, String message){
        return "<" + userName + "> " + message;
    }

}

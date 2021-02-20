package space.cubicworld.multichat;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcPlugin;

import java.text.Format;
import java.text.MessageFormat;

public class MultiChatDiscordHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getChannel().getIdLong() !=
                DiscomcPlugin.getDiscomcPlugin().getDiscomcSave().getMultiChatChannelID()) return;
        Bukkit.broadcastMessage(buildMessage(event.getAuthor(), event.getMessage().getContentDisplay()));
    }

    public String buildMessage(User user, String content){
        return MessageFormat.format(
                DiscomcPlugin.getDiscomcPlugin().getMultiChatModule().getConfiguration().getMinecraftMessagePattern(),
                user.getName(), content
        );
    }

}

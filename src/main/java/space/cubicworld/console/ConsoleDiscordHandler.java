package space.cubicworld.console;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.DiscomcUtil;

public class ConsoleDiscordHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcSave discomcSave = discomcPlugin.getDiscomcSave();
        if (event.getAuthor().isBot() || event.getChannel().getIdLong() !=
                discomcSave.getConsoleChannelID()) return;
        DiscomcUtil.scheduleCommand(event.getMessage().getContentRaw());
    }
}

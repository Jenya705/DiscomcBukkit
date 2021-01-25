package space.cubicworld.console;

import ch.jalu.configme.SettingsManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcConfiguration;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.DiscordManager;
import space.cubicworld.service.DiscomcCommandDispatcher;

public class DiscordConsoleHandler extends ListenerAdapter {

    static {
        SettingsManager discomcConfiguration = DiscomcPlugin.getInstance().getDiscomcConfig();
        long consoleCommandExecutionUpdate = discomcConfiguration.getProperty(DiscomcConfiguration.CONSOLE_COMMAND_EXECUTION_UPDATE);
        DiscomcPlugin.addBukkitTask(new DiscomcCommandDispatcher().runTaskTimer(DiscomcPlugin.getInstance(),
                consoleCommandExecutionUpdate, consoleCommandExecutionUpdate));
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        SettingsManager discomcSave = discomcPlugin.getDiscomcSave();
        DiscordManager discordManager = discomcPlugin.getDiscordManager();
        if (event.getMessage().getAuthor().getIdLong() == discordManager.getJda().getSelfUser().getIdLong()) return;
        if (event.getMessage().getTextChannel().getIdLong() != discomcSave.getProperty(DiscomcSave.CONSOLE_CHANNEL_ID)) return;
        DiscomcCommandDispatcher.addCommand(event.getMessage().getContentRaw());
    }
}

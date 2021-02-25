package space.cubicworld.console;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcModule;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.discord.DiscordModule;

import java.util.ArrayDeque;
import java.util.Deque;

public class ConsoleModule implements DiscomcModule {

    private boolean enabled;

    @Getter
    @Setter
    private ConsoleConfiguration config;

    @Getter
    private Deque<String> consoleMessages;

    @Override
    public void load() {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        FileConfiguration fileConfiguration = discomcPlugin.getConfig();
        setConfig(new ConsoleConfiguration(fileConfiguration));
        setEnabled(getConfig().isEnabled());
    }

    @Override
    public void enable() {
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addAppender(new ConsoleAppender("Discomc Appender", null, null));
        consoleMessages = new ArrayDeque<>();
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscordModule discordModule = discomcPlugin.getDiscordModule();
        DiscomcSave discomcSave = discomcPlugin.getDiscomcSave();
        if (discomcSave.getConsoleChannelID() == 0){
            discomcSave.setConsoleChannelID(
                    discordModule.createTextChannel("discomc-console").getIdLong());
        }
        discordModule.getJda().addEventListener(new ConsoleDiscordHandler());
        discomcPlugin.getServer().getScheduler().runTaskTimerAsynchronously(discomcPlugin, () -> {
            TextChannel consoleTextChannel = discordModule.getJda()
                    .getTextChannelById(discomcSave.getConsoleChannelID());
            if (consoleTextChannel == null) return;
            StringBuilder currentMessage = new StringBuilder();
            while (consoleMessages.peek() != null){
                String logMessage = ConsoleAppender.formatMessage(consoleMessages.pop());
                if (currentMessage.length() + logMessage.length() > 2000){
                    consoleTextChannel.sendMessage(currentMessage.toString()).queue();
                    currentMessage = new StringBuilder();
                }
                currentMessage.append(logMessage).append("\n");
            }
            if (currentMessage.length() != 0) consoleTextChannel.sendMessage(currentMessage.toString()).queue();
        }, getConfig().getLogEventsSendDelay(), getConfig().getLogEventsSendDelay());
    }

    @Override
    public void disable() {
        /* NOTHING */
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getDescription() {
        return "like console but not console ¯\\_(ツ)_/¯";
    }
}

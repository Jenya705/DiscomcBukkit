package space.cubicworld.console;

import ch.jalu.configme.SettingsManager;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.scheduler.BukkitRunnable;
import space.cubicworld.DiscomcConfiguration;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.DiscordManager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;

public class DiscomcConsoleMessagesSender extends BukkitRunnable {

    private static Deque<String> messagesDeque = new ArrayDeque<>();

    @Override
    public void run() {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        while (true){
            SettingsManager discomcConfig = discomcPlugin.getDiscomcConfig();
            SettingsManager discomcSave = discomcPlugin.getDiscomcSave();
            DiscordManager discordManager = discomcPlugin.getDiscordManager();
            TextChannel consoleTextChannel = discordManager.getJda().getTextChannelById(discomcSave.getProperty(DiscomcSave.CONSOLE_CHANNEL_ID));
            String finalMessage = "";
            while (messagesDeque.peek() != null){
                String message = messagesDeque.pop();
                if (discomcConfig.getProperty(DiscomcConfiguration.CONSOLE_FORMATTING)) message = formatMessage(message);
                finalMessage += message + "\n";
            }
            if (!finalMessage.isEmpty()) consoleTextChannel.sendMessage(finalMessage).queue();
            try {
                Thread.sleep(discomcConfig.getProperty(DiscomcConfiguration.CONSOLE_UPDATE));
            } catch (InterruptedException e){
                discomcPlugin.getLogger().log(Level.WARNING, "Exception while trying to set delay between check for sending console messages:", e);
            }
        }
    }

    public String formatMessage(String message){
        /**
         * Symbols like * will replace to \*
         */
        String result = "";
        for (int i = 0; i < message.length(); ++i){
            char ch = message.charAt(i);
            switch (ch){
                case '*':
                case '~':
                case '_':
                case '|':
                case '`':
                    result += "\\" + ch;
                    break;
                default:
                    result += ch;
                    break;
            }
        }
        return result;
    }

    public static void addMessage(String message){
        messagesDeque.add(message);
    }

}

package space.cubicworld.shortcut;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcCommandSender;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcUtil;
import space.cubicworld.discord.DiscordModule;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class ShortcutDiscordHandler extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscordModule discordModule = discomcPlugin.getDiscordModule();
        ShortcutModule shortcutModule = discomcPlugin.getShortcutModule();
        String contentRaw = event.getMessage().getContentRaw();
        User author = event.getAuthor();
        if (author.isBot() || event.getChannel().getGuild().getIdLong() != discordModule.getConfig().getMainServerID() ||
                !contentRaw.startsWith(shortcutModule.getConfig().getCommandsPrefix())) return;
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        String shortcut = null;
        String args = null;
        String commandsPrefix = shortcutModule.getConfig().getCommandsPrefix();
        for (int i = commandsPrefix.length(); i < contentRaw.length(); ++i){
            if (contentRaw.charAt(i) == ' '){
                shortcut = contentRaw.substring(commandsPrefix.length(), i);
                args = contentRaw.substring(i);
                break;
            }
        }
        if (shortcut == null) { // so args is null
            shortcut = contentRaw.substring(commandsPrefix.length());
            args = "";
        }
        String command = shortcutModule.getShortcuts().getOrDefault(shortcut.toLowerCase(), null);
        if (command == null){
            MessageAction action = event.getChannel().sendMessage(MessageFormat.format(
                    discomcMessages.getShortcutCommandNotExist(), author.getAsMention()
            ));
            if (shortcutModule.getConfig().getMessageDeleteTime() != -1){
                action.delay(shortcutModule.getConfig().getMessageDeleteTime(), TimeUnit.SECONDS)
                        .queue(it -> it.delete().queue());
            }
            else {
                action.queue();
            }
            return;
        }

        OfflinePlayer player = DiscomcUtil.getPlayer(author.getIdLong());
        if (player == null){
            MessageAction action = event.getChannel().sendMessage(MessageFormat.format(
                    discomcMessages.getShortcutNeedToBeConnected(), author.getAsMention()
            ));
            if (shortcutModule.getConfig().getMessageDeleteTime() != -1){
                action.delay(shortcutModule.getConfig().getMessageDeleteTime(), TimeUnit.SECONDS)
                        .queue(it -> it.delete().queue());
            }
            else {
                action.queue();
            }
            return;
        }

        Message message = event.getChannel().sendMessage(shortcut + ":").complete();
        if (shortcutModule.getConfig().getResponseDeleteTime() != -1){
            message.delete().queueAfter(shortcutModule.getConfig()
                    .getResponseDeleteTime(), TimeUnit.SECONDS);
        }
        DiscomcUtil.scheduleCommand(new DiscomcCommandSender(player, author, message), command + args);
    }

}

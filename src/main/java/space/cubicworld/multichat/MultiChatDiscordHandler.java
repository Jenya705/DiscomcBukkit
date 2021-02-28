package space.cubicworld.multichat;

import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcUtil;
import space.cubicworld.DiscomcVaultHook;
import space.cubicworld.connect.ConnectModule;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MultiChatDiscordHandler extends ListenerAdapter {


    public MultiChatDiscordHandler(){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getChannel().getIdLong() !=
                DiscomcPlugin.getDiscomcPlugin().getDiscomcSave().getMultiChatChannelID()) return;
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        ConnectModule connectModule = discomcPlugin.getConnectModule();
        MultiChatModule multiChatModule = discomcPlugin.getMultiChatModule();
        User author = event.getAuthor();
        if (multiChatModule.getConfig().isVaultFeatures() && discomcPlugin.getVaultHook().isChat()) {
            OfflinePlayer player = DiscomcUtil.getPlayer(author.getIdLong());
            if (player == null) {
                Bukkit.broadcastMessage(buildMessage(author, event.getMessage().getContentDisplay()));
            } else {
                Bukkit.broadcastMessage(buildMessage(player, author, event.getMessage().getContentDisplay()));
            }
        } else {
            Bukkit.broadcastMessage(buildMessage(author, event.getMessage().getContentDisplay()));
        }
    }

    public String buildMessage(User user, String content){
        return MessageFormat.format(
                DiscomcPlugin.getDiscomcPlugin().getMultiChatModule().getConfig().getMinecraftMessagePattern(),
                user.getName(), content
        );
    }

    public String buildMessage(OfflinePlayer player, User user, String content){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcVaultHook vaultHook = discomcPlugin.getVaultHook();
        MultiChatModule multiChatModule = discomcPlugin.getMultiChatModule();
        String worldName = Bukkit.getWorlds().get(0).getName();
        String prefix = vaultHook.getChat().getPlayerPrefix(worldName, player);
        String suffix = vaultHook.getChat().getPlayerSuffix(worldName, player);
        if (prefix == null) prefix = "";
        else prefix = prefix.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
        if (suffix == null) suffix = "";
        else suffix = suffix.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
        return MessageFormat.format(
                multiChatModule.getConfig().getMinecraftMessagePattern(),
                prefix + user.getName() + suffix, content
        );
    }

}

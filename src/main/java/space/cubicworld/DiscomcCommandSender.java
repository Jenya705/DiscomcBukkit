package space.cubicworld;

import lombok.Data;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.console.ConsoleAppender;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Data
public class DiscomcCommandSender implements RemoteConsoleCommandSender {

    private OfflinePlayer player;
    private User user;
    private Message responseMessage;

    public DiscomcCommandSender(OfflinePlayer player, User user, Message responseMessage){
        setPlayer(player);
        setUser(user);
        setResponseMessage(responseMessage);
    }

    @Override
    public void sendMessage(@NotNull String msg) {
        String contentRaw = responseMessage.getContentRaw();
        msg = ConsoleAppender.formatMessage(msg);
        if (contentRaw.length() + msg.length() > 2000){
            responseMessage.getChannel().sendMessage(msg).queue();
        }
        else {
            responseMessage.editMessage(contentRaw + "\n" + msg).queue();
        }
    }

    @Override
    public void sendMessage(@NotNull String[] strings) {
        StringBuilder messageBuilder = new StringBuilder();
        for (String str: strings) messageBuilder.append(str).append("\n");
        sendMessage(messageBuilder.toString());
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
        sendMessage(s);
    }

    @Override
    public void sendMessage(@Nullable UUID uuid, @NotNull String[] strings) {
        sendMessage(strings);
    }

    @Override
    public @NotNull Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public @NotNull String getName() {
        return getUser().getName();
    }

    @Override
    public @NotNull Spigot spigot() {
        Player onlinePlayer = getPlayer().getPlayer();
        if (onlinePlayer != null){
            return onlinePlayer.spigot();
        }
        throw new NullPointerException("Player is not online");
    }

    @Override
    public boolean isPermissionSet(@NotNull String s) {
        return false;
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission permission) {
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        Player onlinePlayer = getPlayer().getPlayer();
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcDefaultConfiguration defaultConfiguration = discomcPlugin.getDefaultConfiguration();
        DiscomcVaultHook vaultHook = discomcPlugin.getVaultHook();
        if (isOp()){
            return true;
        }
        else if (onlinePlayer != null){
            return onlinePlayer.hasPermission(permission);
        }
        else if (defaultConfiguration.isUseVaultFeaturesForPermissions() && vaultHook.isPermission()) {
            return vaultHook.getPermission().playerHas(Bukkit.getWorlds().get(0).getName(), getPlayer(), permission);
        }
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        return hasPermission(permission.getName());
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
        Player onlinePlayer = getPlayer().getPlayer();
        if (onlinePlayer != null) {
            return onlinePlayer.addAttachment(plugin, s, b);
        }
        DiscomcPlugin.logger().warning("Something trying to add attachment for not online player using DiscomcCommandSender!");
        throw new NullPointerException("You can not invoke this command for not online player");
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        Player onlinePlayer = getPlayer().getPlayer();
        if (onlinePlayer != null) {
            return onlinePlayer.addAttachment(plugin);
        }
        DiscomcPlugin.logger().warning("Something trying to add attachment for not online player using DiscomcCommandSender!");
        throw new NullPointerException("You can not invoke this command for not online player");
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        Player onlinePlayer = getPlayer().getPlayer();
        if (onlinePlayer != null) {
            return onlinePlayer.addAttachment(plugin, s, b, i);
        }
        DiscomcPlugin.logger().warning("Something trying to add attachment for not online player using DiscomcCommandSender!");
        return null;
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
        Player onlinePlayer = getPlayer().getPlayer();
        if (onlinePlayer != null) {
            return onlinePlayer.addAttachment(plugin, i);
        }
        DiscomcPlugin.logger().warning("Something trying to add attachment for not online player using DiscomcCommandSender!");
        return null;
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
        Player onlinePlayer = getPlayer().getPlayer();
        if (onlinePlayer != null) {
            onlinePlayer.removeAttachment(permissionAttachment);
            return;
        }
        DiscomcPlugin.logger().warning("Something trying to remove attachment for not online player using DiscomcCommandSender!");
    }

    @Override
    public void recalculatePermissions() {
        Player onlinePlayer = getPlayer().getPlayer();
        if (onlinePlayer != null) {
            onlinePlayer.recalculatePermissions();
            return;
        }
        DiscomcPlugin.logger().warning("Something trying to recalculate permission for not online player using DiscomcCommandSender!");
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        Player onlinePlayer = getPlayer().getPlayer();
        if (onlinePlayer != null){
            return onlinePlayer.getEffectivePermissions();
        }
        return Collections.emptySet();
    }

    @Override
    public boolean isOp() {
        return getPlayer().isOp();
    }

    @Override
    public void setOp(boolean op) {
        getPlayer().setOp(op);
    }
}

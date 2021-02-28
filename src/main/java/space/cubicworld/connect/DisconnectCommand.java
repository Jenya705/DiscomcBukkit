package space.cubicworld.connect;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DisconnectCommand implements CommandExecutor {

    private List<UUID> confirm = null;

    public DisconnectCommand(){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        ConnectModule connectModule = discomcPlugin.getConnectModule();
        if (connectModule.getConfig().isDisconnectConfirm()){
            confirm = new ArrayList<>();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        ConnectModule connectModule = discomcPlugin.getConnectModule();
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            discomcPlugin.getServer().getScheduler().runTaskAsynchronously(discomcPlugin, () -> {
                DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
                if (connectModule.getConfig().isDisconnectConfirm()) {
                    if (confirm.contains(player.getUniqueId())){
                        disconnect(player);
                    }
                    else {
                        confirm.add(player.getUniqueId());
                        player.sendMessage(discomcMessages.getDisconnectConfirm());
                    }
                } else {
                    disconnect(player);
                }
            });
        }
        return true;
    }

    private void disconnect(Player player){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        Long discordID = ConnectModule.getDiscordId(player.getUniqueId());
        if (discordID == null){
            player.sendMessage(discomcMessages.getDisconnectNotConnected());
        }
        else if (discordID != -1){
            boolean success = ConnectModule.deletePlayer(player.getUniqueId());
            if (success) {
                player.sendMessage(discomcMessages.getDisconnectSuccess());
            }
            else {
                player.sendMessage(discomcMessages.getSqlExceptionMinecraft());
            }
        }
        else {
            player.sendMessage(discomcMessages.getSqlExceptionMinecraft());
        }
        if (confirm != null) confirm.remove(player.getUniqueId());
    }

}

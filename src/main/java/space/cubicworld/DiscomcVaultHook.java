package space.cubicworld;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

@Getter
public class DiscomcVaultHook {

    private Chat chat;
    private Permission permission;

    public DiscomcVaultHook(){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        if (discomcPlugin.getServer().getPluginManager().getPlugin("Vault") == null) return;
        discomcPlugin.getLogger().info("Vault found!");
        RegisteredServiceProvider<Chat> rspChat = discomcPlugin.getServer()
                .getServicesManager().getRegistration(Chat.class);
        if (rspChat != null) chat = rspChat.getProvider();
        RegisteredServiceProvider<Permission> rspPermission = discomcPlugin.getServer()
                .getServicesManager().getRegistration(Permission.class);
        if (rspPermission != null) permission = rspPermission.getProvider();
    }

    public boolean isChat(){
        return chat != null;
    }

    public boolean isPermission() {
        return permission != null;
    }

}

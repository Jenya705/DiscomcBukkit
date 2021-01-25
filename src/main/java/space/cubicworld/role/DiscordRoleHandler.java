package space.cubicworld.role;

import ch.jalu.configme.SettingsManager;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import space.cubicworld.DiscomcConfiguration;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcRoles;
import space.cubicworld.service.DiscomcCommandDispatcher;
import space.cubicworld.DiscomcDatabase;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DiscordRoleHandler extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        SettingsManager discomcConfig = DiscomcPlugin.getInstance().getDiscomcConfig();
        roleChange(discomcConfig.getProperty(DiscomcConfiguration.DISCORD_MINECRAFT_ROLE_GIVE_PATTERN),
                event.getRoles(), event.getMember().getIdLong());
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        SettingsManager discomcConfig = DiscomcPlugin.getInstance().getDiscomcConfig();
        roleChange(discomcConfig.getProperty(DiscomcConfiguration.DISCORD_MINECRAFT_ROLE_REMOVE_PATTERN),
                event.getRoles(), event.getMember().getIdLong());
    }

    protected void roleChange(String pattern, List<Role> roles, Long discordID){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        try {
            DiscomcRoles discomcRoles = discomcPlugin.getDiscomcRoles();
            DiscomcDatabase discomcDatabase = discomcPlugin.getDiscomcDatabase();
            UUID playerUUID = discomcDatabase.getPlayerByDiscordID(discordID);
            if (playerUUID == null) return;
            Player player = Bukkit.getPlayer(playerUUID);
            for (Role role: roles) {
                String mcRole = discomcRoles.getRole(role.getIdLong());
                if (mcRole == null) continue;
                DiscomcCommandDispatcher.addCommand(
                        MessageFormat.format(pattern, player.getName(), mcRole)
                );
            }
        } catch(SQLException e){
            discomcPlugin.getLogger().log(Level.SEVERE, "Exception while changing the role", e);
        }
    }
}

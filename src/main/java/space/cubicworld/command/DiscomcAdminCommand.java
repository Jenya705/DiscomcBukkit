package space.cubicworld.command;

import lombok.Data;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;

@Data
public abstract class DiscomcAdminCommand implements CommandExecutor, TabExecutor {

    private String description;
    private String permission;

    public DiscomcAdminCommand(String description, String permission){
        setDescription(description);
        setPermission(permission);
    }

}

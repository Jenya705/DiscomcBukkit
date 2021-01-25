package com.github.jenya705.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandExecutor;

@Getter
@Setter
public abstract class AdminCommand implements CommandExecutor {

    private String name;
    private String help;
    private String permission;

    public AdminCommand(String name, String help, String permission){
        setName(name);
        setHelp(help);
        setPermission(permission);
    }

}

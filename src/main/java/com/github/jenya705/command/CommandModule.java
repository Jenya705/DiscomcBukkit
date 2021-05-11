package com.github.jenya705.command;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.util.ConfigsUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;

@Getter
@Setter(AccessLevel.PROTECTED)
public class CommandModule implements DiscomcModule {

    private CommandConfig config;

    @Override
    @SneakyThrows
    public void onLoad() {
        Discomc discomc = Discomc.getPlugin();
        setConfig(ConfigsUtil.loadConfig(new CommandConfig(),
                new File(discomc.getDataFolder(), "command.yml")));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void disable() {
        Discomc discomc = Discomc.getPlugin();
        discomc.getLogger().severe("[Command] Command module disabled, disabling plugin");
        discomc.getServer().getPluginManager().disablePlugin(discomc);
    }
}

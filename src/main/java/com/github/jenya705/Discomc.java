package com.github.jenya705;

import com.github.jenya705.data.DataFactory;
import com.github.jenya705.data.MultiDataFactory;
import com.github.jenya705.data.SerializedData;
import com.github.jenya705.discord.DiscordModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Getter
@Setter(AccessLevel.PROTECTED)
public final class Discomc extends JavaPlugin {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static Discomc plugin;

    /**
     * All private final fields is private because
     * I want to make it not final so it can be changed
     * in runtime, that is good i think
     */

    private final DataFactory dataFactory = new MultiDataFactory();

    private SerializedData dataConfig;
    private Map<String, DiscomcModule> modules;
    private DiscordModule discordModule;

    @SneakyThrows
    @Override
    public void onEnable() {
        setPlugin(this);
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
        setDataConfig(getDataFactory().createData(configFile));
        setModules(new HashMap<>());
        setDiscordModule(addModule(new DiscordModule(), "Discord"));
        /* Loading modules */ getModules().forEach((name, module) -> {
            try {
                module.onLoad();
            } catch (Exception e){
                getLogger().log(Level.WARNING, String.format("%s module throw Exception, disable it:", name), e);
                module.disable();
            }
        });
        /* Enabling modules */ getModules().forEach((name, module) -> {
            try {
                if (module.isEnabled()) {
                    module.onEnable();
                }
            } catch (Exception e){
                getLogger().log(Level.WARNING, String.format("%s module throw Exception, disable it:", name), e);
                module.disable();
            }
        });
    }

    @Override
    public void onDisable() {
        /* Disabling modules */ getModules().forEach((name, module) -> {
            try {
                if (module.isEnabled()){
                    module.onDisable();
                }
            } catch (Exception e){
                getLogger().log(Level.WARNING, String.format("%s module throw Exception:", name), e);
            }
        });
    }

    private <T extends DiscomcModule> T addModule(T module, String name){
        getModules().put(name, module);
        return module;
    }

}

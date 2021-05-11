package com.github.jenya705;

import com.github.jenya705.command.CommandModule;
import com.github.jenya705.connect.ConnectModule;
import com.github.jenya705.console.ConsoleModule;
import com.github.jenya705.data.DataFactory;
import com.github.jenya705.data.DataType;
import com.github.jenya705.data.MultiDataFactory;
import com.github.jenya705.data.SerializedData;
import com.github.jenya705.database.DatabaseModule;
import com.github.jenya705.discord.DiscordModule;
import com.github.jenya705.multichat.MultiChatModule;
import com.github.jenya705.nickname.NicknameModule;
import com.github.jenya705.scheduler.BukkitDiscomcScheduler;
import com.github.jenya705.scheduler.DiscomcScheduler;
import com.github.jenya705.service.AsyncCommandsService;
import com.github.jenya705.service.DiscomcService;
import com.github.jenya705.skin.CrafatarHeadURLFactory;
import com.github.jenya705.skin.HeadURLFactory;
import com.github.jenya705.uuid.MojangUUIDFactory;
import com.github.jenya705.uuid.UUIDFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Getter
@Setter(AccessLevel.PROTECTED)
public final class Discomc extends JavaPlugin {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static Discomc plugin;

    // Factories
    private UUIDFactory uuidFactory;
    private DataFactory dataFactory;
    private HeadURLFactory headURLFactory;
    private DiscomcScheduler scheduler;

    // Config
    private SerializedData dataConfig;
    private DefaultConfig defaultConfig;

    // Modules
    private Map<String, DiscomcModule> modules;
    private DiscordModule discordModule;
    private MultiChatModule multiChatModule;
    private DatabaseModule databaseModule;
    private ConnectModule connectModule;
    private ConsoleModule consoleModule;
    private NicknameModule nicknameModule;
    private CommandModule commandModule;

    // Services
    private AsyncCommandsService asyncCommandsService;

    @Override
    public void onLoad() {
        setUuidFactory(new MojangUUIDFactory());
        setDataFactory(new MultiDataFactory());
        setHeadURLFactory(new CrafatarHeadURLFactory());
        setScheduler(new BukkitDiscomcScheduler());
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        setPlugin(this);
        getCoreLogger().addFilter(new Discord4JFilter());
        getDataFolder().mkdir();
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
        // configs load
        setDataConfig(getDataFactory().createData(configFile, DataType.YAML));
        setDefaultConfig(new DefaultConfig());
        getDefaultConfig().load(getDataConfig());
        getDefaultConfig().save(getDataConfig());
        // Creating service instances
        setAsyncCommandsService(addService(new AsyncCommandsService()));
        // Creating module instances
        setModules(new HashMap<>());
        setDiscordModule(addModule(new DiscordModule(), "Discord"));
        setDatabaseModule(addModule(new DatabaseModule(), "Database"));
        setMultiChatModule(addModule(new MultiChatModule(), "MultiChat"));
        setConnectModule(addModule(new ConnectModule(), "Connect"));
        setConsoleModule(addModule(new ConsoleModule(), "Console"));
        setNicknameModule(addModule(new NicknameModule(), "Nickname"));
        setCommandModule(addModule(new CommandModule(), "Command"));
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
        saveConfig();
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

    public <T extends DiscomcModule> T addModule(T module, String name){
        getModules().put(name, module);
        return module;
    }

    public <T extends DiscomcService> T addService(T instance) {
        instance.setup();
        return instance;
    }

    @SneakyThrows
    public void saveConfig() {
        Files.write(new File(getDataFolder(), "config.yml").toPath(), getDataConfig()
                .toSerializedString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE);
    }

    public Logger getCoreLogger(){
        return (Logger) LogManager.getRootLogger();
    }

}

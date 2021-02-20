package space.cubicworld;

import lombok.Getter;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;
import space.cubicworld.connect.ConnectModule;
import space.cubicworld.console.ConsoleModule;
import space.cubicworld.database.DatabaseModule;
import space.cubicworld.discord.DiscordModule;
import space.cubicworld.multichat.MultiChatModule;

import java.io.*;
import java.nio.file.Files;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class DiscomcPlugin extends JavaPlugin {

    public static final Random random = new Random();

    @Getter
    private static DiscomcPlugin discomcPlugin;

    private DiscomcSave discomcSave;
    private DiscomcMessages discomcMessages;
    private DiscomcUtil discomcUtil;

    private MultiChatModule multiChatModule;
    private DiscordModule discordModule;
    private DatabaseModule databaseModule;
    private ConnectModule connectModule;
    private ConsoleModule consoleModule;

    public DiscomcPlugin() throws InvalidPluginException {
        if (discomcPlugin != null) throw new InvalidPluginException("Plugin constructor called twice!");
        discomcPlugin = this;
    }

    public void loadSettings(){
        try {
            createConfigIfNotExist();
            createSaveIfNotExist();
        }
        catch (IOException e) {
            getLogger().log(Level.SEVERE, "Can not create file, crashing plugin:", e);
            setEnabled(false);
        }

        discomcMessages = new DiscomcMessages(new File(getDataFolder(), "messages.properties"));
        discomcSave = DiscomcSave.of(new File(getDataFolder(), "save.json"));
        if (discomcSave != null) discomcSave.save(new File(getDataFolder(), "save.json"));

    }

    @Override
    public void onLoad() {

        loadSettings();

        // vanilla modules start
        discordModule = new DiscordModule();
        databaseModule = new DatabaseModule();
        multiChatModule = new MultiChatModule();
        connectModule = new ConnectModule();
        consoleModule = new ConsoleModule();

        ModuleStore.putModule("database", databaseModule);
        ModuleStore.putModule("discordAPI", discordModule);
        ModuleStore.putModule("multiChat", multiChatModule);
        ModuleStore.putModule("connect", connectModule);
        ModuleStore.putModule("console", consoleModule);
        // vanilla modules end

        ModuleStore.getModules().forEach((name, module) -> {
            try {
                getLogger().info(String.format("Loading %s module", name));
                module.load();
            } catch (Exception e){
                getLogger().log(Level.WARNING, String.format("Disabling %s module because of exception:", name), e);
                module.setEnabled(false);
            }
        });
    }

    @Override
    public void onEnable() {
        discomcUtil = new DiscomcUtil();
        ModuleStore.getModules().forEach((name, module) -> {
            if (!module.isEnabled()) return;
            getLogger().info(String.format("Enabling %s module", name));
            module.enable();
        });
    }

    @Override
    public void onDisable() {
        getDiscomcSave().save(new File(getDataFolder(), "save.json"));
        ModuleStore.getModules().forEach((name, module) -> {
            if (!module.isEnabled()) return;
            getLogger().info(String.format("Disabling %s module", name));
            module.disable();
        });
    }

    public void createConfigIfNotExist() throws IOException {
        File configFile = new File(getDataFolder(), "config.yml");
        if (configFile.exists()) return;
        getDataFolder().mkdirs();
        configFile.createNewFile();
        InputStream resourceConfig = getClassLoader().getResourceAsStream("config.yml");
        byte[] resourceConfigBytes = new byte[resourceConfig.available()];
        resourceConfig.read(resourceConfigBytes);
        FileWriter configFileInputStream = new FileWriter(configFile);
        configFileInputStream.write(new String(resourceConfigBytes));
        configFileInputStream.flush();
    }

    public void createSaveIfNotExist() throws IOException {
        File saveFile = new File(getDataFolder(), "save.json");
        if (saveFile.exists()) return;
        Files.write(saveFile.toPath(), "{}".getBytes());
    }

    public static void pluginEnabled(boolean enabled){
        if (!enabled) logger().warning("Discomc plugin is disabled!");
        getDiscomcPlugin().setEnabled(enabled);
    }

    public static Logger logger(){
        return getDiscomcPlugin().getLogger();
    }

}

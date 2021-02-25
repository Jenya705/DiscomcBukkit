package space.cubicworld.connect;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import space.cubicworld.DiscomcModule;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.discord.DiscordModule;

import java.util.HashMap;
import java.util.Map;

public class ConnectModule implements DiscomcModule {

    public static final String CONNECT_COMMAND = "connect";

    private boolean enabled;

    @Getter
    private Map<Integer, Player> codes;

    @Getter
    @Setter
    private ConnectConfiguration config;

    @Override
    public void load() {
        FileConfiguration fileConfiguration = DiscomcPlugin.getDiscomcPlugin().getConfig();
        setConfig(new ConnectConfiguration(fileConfiguration));
        setEnabled(getConfig().isEnabled());
    }

    @Override
    public void enable() {
        codes = new HashMap<>();
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        PluginCommand connectCommand = discomcPlugin.getCommand(CONNECT_COMMAND);
        if (connectCommand != null){
            connectCommand.setExecutor(new ConnectCommand());
        } else {
            discomcPlugin.getLogger().warning("Can not find connect command, disabling connect module!");
            setEnabled(false);
            return;
        }
        DiscordModule discordModule = discomcPlugin.getDiscordModule();
        DiscomcSave discomcSave = discomcPlugin.getDiscomcSave();
        if (discomcSave.getConnectChannelID() == 0) {
            discomcPlugin.getDiscomcSave().setConnectChannelID(
                    discordModule.createTextChannel("discomc-connect").getIdLong());
        }
        discordModule.getJda().addEventListener(new ConnectDiscordHandler());
    }

    @Override
    public void disable() {
        /* NOTHING */
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getDescription() {
        return "Module which link minecraft and discord accounts";
    }



}

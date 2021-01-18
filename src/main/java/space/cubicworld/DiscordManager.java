package space.cubicworld;

import ch.jalu.configme.SettingsManager;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import space.cubicworld.discord.DiscordChatHandler;

import javax.security.auth.login.LoginException;

@Getter
@Setter
public class DiscordManager {

    private JDA jda;

    public DiscordManager() throws LoginException {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        setJda(JDABuilder
                .createDefault(discomcPlugin.getDiscomcConfig().getProperty(DiscomcConfiguration.BOT_TOKEN))
                .addEventListeners(
                        new DiscordChatHandler()
                )
                .build()
        );
    }

    protected void checkForChannelExist(){

        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        SettingsManager discomcSave = discomcPlugin.getDiscomcSave();
        SettingsManager discomcConfig = discomcPlugin.getDiscomcConfig();

        Guild mainGuild = getJda().getGuildById(discomcConfig.getProperty(DiscomcConfiguration.MAIN_SERVER_ID));

        if (discomcSave.getProperty(DiscomcSave.CONSOLE_CHANNEL_ID) == -1){

        }

        if (discomcSave.getProperty(DiscomcSave.CONNECT_CHANNEL_ID) == -1){

        }

        if (discomcSave.getProperty(DiscomcSave.CHAT_CHANNEL_ID) == -1){

        }

    }

}

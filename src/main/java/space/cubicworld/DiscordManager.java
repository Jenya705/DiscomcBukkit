package space.cubicworld;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import space.cubicworld.discord.DiscordChatHandler;
import space.cubicworld.discord.DiscordConnectHandler;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

@Getter
@Setter
public class DiscordManager {

    private JDA jda;

    public DiscordManager() throws LoginException, InterruptedException {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        JDABuilder jdaBuilder = JDABuilder.createDefault(discomcPlugin.getDiscomcConfig().getProperty(DiscomcConfiguration.BOT_TOKEN));

        if (discomcPlugin.getDiscomcConfig().getProperty(DiscomcConfiguration.MULTI_CHAT_ENABLED)) jdaBuilder.addEventListeners(new DiscordChatHandler());
        if (discomcPlugin.getDiscomcConfig().getProperty(DiscomcConfiguration.CONNECT_ENABLED))    jdaBuilder.addEventListeners(new DiscordConnectHandler());

        setJda(jdaBuilder.build());
        getJda().awaitReady();
        checkForChannelsExist();
    }

    protected void checkForChannelsExist(){

        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        SettingsManager discomcSave = discomcPlugin.getDiscomcSave();
        SettingsManager discomcConfig = discomcPlugin.getDiscomcConfig();

        Guild mainGuild = getJda().getGuildById(discomcConfig.getProperty(DiscomcConfiguration.MAIN_SERVER_ID));
        long categoryID = discomcConfig.getProperty(DiscomcConfiguration.CREATION_CATEGORY_ID);
        Category category;

        if (categoryID == -1) {
            category = mainGuild.createCategory("discomc").
                    addRolePermissionOverride(mainGuild.getRoles().get(mainGuild.getRoles().size() - 1).getIdLong(),
                    Arrays.asList(), Arrays.asList(Permission.MESSAGE_READ)).complete();
            discomcConfig.setProperty(DiscomcConfiguration.CREATION_CATEGORY_ID, category.getIdLong());
        }
        else {
            category = getJda().getCategoryById(categoryID);
        }

        if (discomcSave.getProperty(DiscomcSave.CONSOLE_CHANNEL_ID) == -1){
            createChannelAndSave(category, discomcSave, DiscomcSave.CONSOLE_CHANNEL_ID, "mc-console");
        }

        if (discomcSave.getProperty(DiscomcSave.CONNECT_CHANNEL_ID) == -1 && discomcConfig.getProperty(DiscomcConfiguration.CONNECT_ENABLED)){
            createChannelAndSave(category, discomcSave, DiscomcSave.CONNECT_CHANNEL_ID, "mc-connect");
        }

        if (discomcSave.getProperty(DiscomcSave.CHAT_CHANNEL_ID) == -1 && discomcConfig.getProperty(DiscomcConfiguration.MULTI_CHAT_ENABLED)){
            createChannelAndSave(category, discomcSave, DiscomcSave.CHAT_CHANNEL_ID, "mc-chat");
        }

    }

    private void createChannelAndSave(Category category, SettingsManager discomcSave, Property<Long> property, String name){
        TextChannel textChannel = category.createTextChannel(name).complete();
        discomcSave.setProperty(property, textChannel.getIdLong());
    }


}

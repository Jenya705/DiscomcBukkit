package space.cubicworld.discord;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import space.cubicworld.DiscomcModule;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.logging.Level;

@Getter
public class DiscordModule implements DiscomcModule {

    private JDA jda;

    @Setter
    private DiscordConfiguration config;

    @Override
    public void load() {
        if (getJda() != null) {
            DiscomcPlugin.logger().warning("Discord module can not be reload!");
            return;
        }
        try {
            setConfig(new DiscordConfiguration(DiscomcPlugin.getDiscomcPlugin().getConfig()));
            JDABuilder jdaBuilder = JDABuilder.createDefault(getConfig().getToken());
            jda = jdaBuilder.build();
            jda.awaitReady();
            DiscomcSave discomcSave = DiscomcPlugin.getDiscomcPlugin().getDiscomcSave();
            if (discomcSave.getCategoryChannelID() == 0){
                Guild mainGuild = getJda().getGuildById(getConfig().getMainServerID());
                discomcSave.setCategoryChannelID(mainGuild
                        .createCategory("discomc-category")
                        .addRolePermissionOverride(mainGuild.getRoles().get(mainGuild.getRoles().size()-1).getIdLong(),
                                Collections.emptyList(), Collections.singletonList(Permission.MESSAGE_READ))
                        .complete().getIdLong());
            }
        } catch (LoginException | InterruptedException e){
            DiscomcPlugin.logger().log(Level.SEVERE, "Exception while login to discord api:", e);
            DiscomcPlugin.pluginEnabled(false);
        }
    }

    @Override
    public void enable() {
        /* NOTHING */
    }

    @Override
    public void disable() {
        getJda().shutdown();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        /* NOTHING */
    }

    @Override
    public String getDescription() {
        return "discord api module";
    }

    public TextChannel createTextChannel(String name){
        DiscomcSave discomcSave = DiscomcPlugin.getDiscomcPlugin().getDiscomcSave();
        return getJda().getCategoryById(discomcSave.getCategoryChannelID())
                .createTextChannel(name).complete();
    }

}

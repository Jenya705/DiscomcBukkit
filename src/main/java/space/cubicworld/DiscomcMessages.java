package space.cubicworld;

import lombok.Data;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

@Data
public class DiscomcMessages {

    private String connectSuccessDiscord = "{0}, You successfully connected";
    private String connectFailedDiscord = "{0}, This code does not exist, did you make mistake?";
    private String connectSuccessMinecraft = "You linked your account with &b{0}&r discord account";
    private String connectTimeEndMinecraft = "&cCode deleted because of time";
    private String connectRequestMinecraft = "Your code is &a{0}&r, write it in &b{1}";
    private String connectAlreadyMinecraft = "You are already connected with &b{0}";
    private String sqlExceptionDiscord = "{0}, Database services is not available! Report to administrators!";
    private String sqlExceptionMinecraft = "Database services is not available! Report to administrators!";

    public DiscomcMessages(File messagesFile){
        try {
            if (!messagesFile.exists()) {
                messagesFile.createNewFile();
                DiscomcPlugin.logger().warning("Messages file is not exist! Using default messages");
            }
            loadMessages(messagesFile);
        } catch (IOException e){
            DiscomcPlugin.logger().log(Level.WARNING, "Can not load messages, using default messages:", e);
        }
    }

    private void loadMessages(File messagesFile) throws IOException {
        Properties messagesProperties = new Properties();
        messagesProperties.load(new FileInputStream(messagesFile));
        setConnectSuccessDiscord(getProperty(messagesProperties, "connect.discord.success", getConnectSuccessDiscord()));
        setConnectFailedDiscord(getProperty(messagesProperties, "connect.discord.failed", getConnectFailedDiscord()));
        setConnectSuccessMinecraft(getProperty(messagesProperties, "connect.minecraft.success", getConnectSuccessMinecraft()));
        setConnectTimeEndMinecraft(getProperty(messagesProperties, "connect.minecraft.time.end", getConnectTimeEndMinecraft()));
        setConnectRequestMinecraft(getProperty(messagesProperties, "connect.minecraft.request", getConnectRequestMinecraft()));
        setConnectAlreadyMinecraft(getProperty(messagesProperties, "connect.minecraft.already", getConnectAlreadyMinecraft()));
        setSqlExceptionDiscord(getProperty(messagesProperties, "exception.discord.sql", getSqlExceptionDiscord()));
        setSqlExceptionMinecraft(getProperty(messagesProperties, "exception.minecraft.sql", getSqlExceptionMinecraft()));
        messagesProperties.store(new FileOutputStream(messagesFile), "Discomc-messages");
    }

    private String getProperty(Properties messageProperties, String key, String standard){
        String property = messageProperties.getProperty(key);
        if (property == null){
            messageProperties.setProperty(key, standard);
            return standard.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
        }
        return property.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
    }

}

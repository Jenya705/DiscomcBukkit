package space.cubicworld;

import lombok.Data;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Properties;

@Data
public class DiscomcMessages {

    private String prefix = "&7&l[&b&lDisco&a&lmc&7&l]";
    private String codeNotNumber = "{0}, write number not text!";
    private String codeNotExist = "{0}, this code doesn't exist!";
    private String connectSuccess = "{0}, successfully linked accounts!";
    private String connectMessage = "Your code is {0}, write it in {1}";
    private String connectSuccessInMinecraft = "You successfully connected!";
    private String alreadyConnected = "You are already connected!";
    private String alreadyWroteConnect = "You are already written connect command!";
    private String connectDisabled = "Connect command disabled!";
    private String notAllowedCommand = "You do not have permission to do this!";
    private String reloaded = "Reloaded!";

    public DiscomcMessages() throws IOException, IllegalAccessException {

        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        File messagesFile = new File(discomcPlugin.getDataFolder(), "messages.properties");
        if (messagesFile.exists()) {
            Properties properties = new Properties();
            properties.load(new FileInputStream(messagesFile));
            Field[] fields = getClass().getDeclaredFields();
            for (Field field: fields){
                String property = properties.getProperty(field.getName());
                if (property == null) continue;
                property = property.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
                field.set(this, property);
            }
            for (Field field: fields){
                properties.setProperty(field.getName(), (String) field.get(this));
            }
            properties.store(new FileWriter(messagesFile), "Discomc messages.");
        }
        else {
            messagesFile.createNewFile();
            Properties properties = new Properties();
            Field[] fields = getClass().getDeclaredFields();
            for (Field field: fields){
                properties.setProperty(field.getName(), (String) field.get(this));
            }
            properties.store(new FileWriter(messagesFile), "Discomc messages.");
        }

    }

    public String getMessage(String msg, boolean prefixed, String... objects){
        if (prefixed) {
            if (prefix.length() == 0) return MessageFormat.format(msg, objects);
            else return prefix + ChatColor.RESET + " " + MessageFormat.format(msg, objects);
        }
        return MessageFormat.format(msg, objects);
    }

}

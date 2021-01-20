package space.cubicworld;

import lombok.Data;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;

@Data
public class DiscomcMessages {

    private String prefix = "&7[&bDisco&amc&7]";
    private String codeNotNumber = "{0}, write number not text!";
    private String codeNotExist = "{0}, this code doesn't exist, did you make mistake?";
    private String connectSuccess = "{0}, successfully connect!";
    private String discordMessage = "<{0}> {1}";
    private String connectMessage = "Your code is {0}, write it in {1}";
    private String connectSuccessInMinecraft = "You successfully connected!";
    private String alreadyConnected = "You are already connected!";

    private DiscomcMessages() {}

    public String getMessage(String msg, boolean prefixed, Object... args){
        if (prefixed) {
            if (prefix.length() != 0) return prefix + ChatColor.RESET + " " + MessageFormat.format(msg, args);
            else return MessageFormat.format(msg, args);
        }
        else {
            return MessageFormat.format(msg, args);
        }
    }

    public static DiscomcMessages load(File messagesFile) throws IOException, IllegalAccessException {

        DiscomcMessages discomcMessages = loadWithoutFormatting(messagesFile);
        Field[] fields = discomcMessages.getClass().getDeclaredFields();
        for (Field field: fields){
            field.setAccessible(true);
            Object value = field.get(discomcMessages);
            if (value instanceof String){
                value = ((String) value).replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
                field.set(discomcMessages, value);
            }
            field.setAccessible(false);
        }
        return discomcMessages;

    }

    protected static DiscomcMessages loadWithoutFormatting(File messagesFile) throws IOException {
        DiscomcMessages discomcMessages;
        if (messagesFile.exists()){
            discomcMessages = DiscomcPlugin.gson.fromJson(new FileReader(messagesFile), DiscomcMessages.class);
        }
        else {
            discomcMessages = new DiscomcMessages();
        }
        String discomcMessagesJson = DiscomcPlugin.gson.toJson(discomcMessages);
        Files.write(messagesFile.toPath(), discomcMessagesJson.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        return discomcMessages;
    }

}

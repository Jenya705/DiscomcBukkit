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

    private String prefix = "[&bDisco&amc&r] ";
    private String connectSuccessDiscord = "{0}, You successfully connected";
    private String connectFailedDiscord = "{0}, This code does not exist, did you make mistake?";
    private String connectSuccessMinecraft = "You linked your account with &b{0}&r discord account";
    private String connectTimeEndMinecraft = "&cCode deleted because of time";
    private String connectRequestMinecraft = "Your code is &a{0}&r, write it in &b{1}";
    private String connectAlreadyMinecraft = "You are already connected with &b{0}";
    private String connectForceIDIsNotNumber = "Discord id {0} is not a number!";
    private String connectForceIOException = "IOException while executing command";
    private String connectForcePlayerIsNotExists = "Premium player {0} is not exist";
    private String connectForceSuccess = "You successfully connect &a{0}&r minecraft user with &b{1}&r discord user";
    private String sqlExceptionDiscord = "{0}, Database services is not available! Report to administrators!";
    private String sqlExceptionMinecraft = "Database services is not available! Report to administrators!";
    private String helpFooter = "[&bDisco&amc&r help | Page {0}]";
    private String helpElement = "| &e{0}&r - &7{1}&r";
    private String helpNext = "--> Next page ]";
    private String helpPrevious = "[ Previous page <--";
    private String commandNotExist = "Command {0} does not exist!";
    private String commandNotPermission = "You do not have permission for this command! Permission: {0}";
    private String userGetInvalidIdentifier = "The identifier {0} is invalid";
    private String userGetAccountsCount = "Found &6{0}&r accounts";
    private String userGetDiscordIDMessage = "Discord id: &b{0}&r";
    private String userGetMinecraftNicknameMessage = "Minecraft nickname: &a{0}&r";
    private String userGetFooter = "&7(Click on nickname to take it)&r";

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
        FileInputStream inputStream = new FileInputStream(messagesFile);
        messagesProperties.load(inputStream);
        inputStream.close();
        setPrefix(getProperty(messagesProperties, "prefix", getPrefix(), false));
        setConnectSuccessDiscord(getProperty(messagesProperties, "connect.discord.success", getConnectSuccessDiscord(), false));
        setConnectFailedDiscord(getProperty(messagesProperties, "connect.discord.failed", getConnectFailedDiscord(), false));
        setConnectSuccessMinecraft(getProperty(messagesProperties, "connect.minecraft.success", getConnectSuccessMinecraft(), true));
        setConnectTimeEndMinecraft(getProperty(messagesProperties, "connect.minecraft.time.end", getConnectTimeEndMinecraft(), true));
        setConnectRequestMinecraft(getProperty(messagesProperties, "connect.minecraft.request", getConnectRequestMinecraft(), true));
        setConnectAlreadyMinecraft(getProperty(messagesProperties, "connect.minecraft.already", getConnectAlreadyMinecraft(), true));
        setConnectForceIDIsNotNumber(getProperty(messagesProperties, "connect.force.idNotNumber", getConnectForceIDIsNotNumber(), true));
        setConnectForceIOException(getProperty(messagesProperties, "connect.force.exception.io", getConnectForceIOException(), true));
        setConnectForcePlayerIsNotExists(getProperty(messagesProperties, "connect.force.player.notExists", getConnectForcePlayerIsNotExists(), true));
        setConnectForceSuccess(getProperty(messagesProperties, "connect.force.success", getConnectForceSuccess(), true));
        setSqlExceptionDiscord(getProperty(messagesProperties, "exception.discord.sql", getSqlExceptionDiscord(), false));
        setSqlExceptionMinecraft(getProperty(messagesProperties, "exception.minecraft.sql", getSqlExceptionMinecraft(), true));
        setHelpFooter(getProperty(messagesProperties, "help.footer", getHelpFooter(), false));
        setHelpElement(getProperty(messagesProperties, "help.element", getHelpElement(), false));
        setHelpNext(getProperty(messagesProperties, "help.next", getHelpNext(), false));
        setHelpPrevious(getProperty(messagesProperties, "help.previous", getHelpPrevious(), false));
        setCommandNotExist(getProperty(messagesProperties, "command.exist.false", getCommandNotExist(), true));
        setCommandNotPermission(getProperty(messagesProperties, "command.permission.false", getCommandNotPermission(), true));
        setUserGetInvalidIdentifier(getProperty(messagesProperties, "userGet.identifier.invalid", getUserGetInvalidIdentifier(), true));
        setUserGetAccountsCount(getProperty(messagesProperties, "userGet.account.count", getUserGetAccountsCount(), false));
        setUserGetDiscordIDMessage(getProperty(messagesProperties, "userGet.account.discord", getUserGetDiscordIDMessage(), false));
        setUserGetMinecraftNicknameMessage(getProperty(messagesProperties, "userGet.account.minecraft", getUserGetMinecraftNicknameMessage(), false));
        setUserGetFooter(getProperty(messagesProperties, "userGet.footer", getUserGetFooter(), false));
        FileOutputStream outputStream = new FileOutputStream(messagesFile);
        messagesProperties.store(outputStream, "Discomc-messages");
        outputStream.close();
    }

    private String getProperty(Properties messageProperties, String key, String standard, boolean prefixed){
        String property = messageProperties.getProperty(key);
        if (property == null){
            messageProperties.setProperty(key, standard);
            if (prefixed) return getPrefix() + standard.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
            else return standard.replace("&", Character.toString(ChatColor.COLOR_CHAR));
        }
        if (prefixed) return getPrefix() + property.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
        else return property.replaceAll("&", Character.toString(ChatColor.COLOR_CHAR));
    }

}

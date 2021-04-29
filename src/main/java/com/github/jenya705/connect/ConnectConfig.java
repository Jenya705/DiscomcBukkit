package com.github.jenya705.connect;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class ConnectConfig implements DiscomcConfig {

    private boolean enabled = true;
    private boolean canEventBeCancelled = true;
    private boolean canEventChangeVariables = true;
    private boolean kickIfNotConnected = false;
    private long connectChannelID = -1;
    private long connectedRoleID = -1;
    private int maxCodeValue = 10000;
    private int codeRemoveTime = 6000;
    private long discordMessageDeleteTime = 5000;
    private String connectRequest = "Your code is &b{0}&r write it in channel &b{1}&r";
    private String codeTimeOver = "&cTime is over, your code is cleared";
    private String codeNotExist = "{0}, this code is not exist";
    private String successfullyConnectedMinecraft = "You connected with &b{0}&r";
    private String successfullyConnectedDiscord = "{0}, success! You are connected!";
    private String kickMessage =    "You need to link your&b discord&r account to play on this server\n" +
                                    "\n" +
                                    "Join&b https://discord.gg/example &r\n" +
                                    "&land&r\n" +
                                    "write in &b&l{0}&r the message &b{1}&r\n";
    private String alreadyConnectedMinecraft = "You are already connected with &b{0}&r";
    private String alreadyConnectedDiscord = "{0}, You are already connected";
    private String eventCancelledDiscord = "{0}, something wrong...";

    @Override
    public void load(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            setAlreadyConnectedMinecraft(minecraftConfigData.getMessage("alreadyConnectedMinecraft", getAlreadyConnectedMinecraft(), true));
            setConnectRequest(minecraftConfigData.getMessage("connectRequest", getConnectRequest(), true));
            setCodeTimeOver(minecraftConfigData.getMessage("codeTimeOver", getCodeTimeOver(), true));
            setSuccessfullyConnectedMinecraft(minecraftConfigData.getMessage(
                    "successfullyConnectedMinecraft", getSuccessfullyConnectedMinecraft(), true));
            setSuccessfullyConnectedDiscord(minecraftConfigData.getMessage(
                    "successfullyConnectedDiscord", getSuccessfullyConnectedDiscord(), false));
            setCodeNotExist(minecraftConfigData.getMessage("codeNotExist", getCodeNotExist(), false));
            setAlreadyConnectedDiscord(minecraftConfigData.getMessage("alreadyConnectedDiscord", getAlreadyConnectedDiscord(), false));
            setEventCancelledDiscord(minecraftConfigData.getMessage("eventCancelledDiscord", getEventCancelledDiscord(), false));
            setKickMessage(minecraftConfigData.getMessage("kickMessage", getKickMessage(), false));
        }
        setEnabled(data.getBoolean("enabled", isEnabled()));
        setCanEventBeCancelled(data.getBoolean("canEventBeCancelled", isCanEventBeCancelled()));
        setCanEventChangeVariables(data.getBoolean("canEventChangeVariables", isCanEventChangeVariables()));
        setKickIfNotConnected(data.getBoolean("kickIfNotConnected", isKickIfNotConnected()));
        setConnectChannelID(data.getLong("connectChannelID", getConnectChannelID()));
        setConnectedRoleID(data.getLong("connectedRoleID", getConnectedRoleID()));
        setMaxCodeValue(data.getInteger("maxCodeValue", getMaxCodeValue()));
        setCodeRemoveTime(data.getInteger("codeRemoveTime", getCodeRemoveTime()));
        setDiscordMessageDeleteTime(data.getLong("discordMessageDeleteTime", getDiscordMessageDeleteTime()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("enabled", isEnabled());
        data.setObject("canEventBeCancelled", isCanEventBeCancelled());
        data.setObject("canEventChangeVariables", isCanEventChangeVariables());
        data.setObject("kickIfNotConnected", isKickIfNotConnected());
        data.setObject("connectChannelID", getConnectChannelID());
        data.setObject("connectedRoleID", getConnectedRoleID());
        data.setObject("maxCodeValue", getMaxCodeValue());
        data.setObject("codeRemoveTime", getCodeRemoveTime());
        data.setObject("discordMessageDeleteTime", getDiscordMessageDeleteTime());
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            minecraftConfigData.setMessage("alreadyConnectedMinecraft", getAlreadyConnectedMinecraft(), true);
            minecraftConfigData.setMessage("connectRequest", getConnectRequest(), true);
            minecraftConfigData.setMessage("codeTimeOver", getCodeTimeOver(), true);
            minecraftConfigData.setMessage("successfullyConnectedMinecraft", getSuccessfullyConnectedMinecraft(), true);
            minecraftConfigData.setMessage("successfullyConnectedDiscord", getSuccessfullyConnectedDiscord(), false);
            minecraftConfigData.setMessage("codeNotExist", getCodeNotExist(), false);
            minecraftConfigData.setMessage("alreadyConnectedDiscord", getAlreadyConnectedDiscord(), false);
            minecraftConfigData.setMessage("eventCancelledDiscord", getEventCancelledDiscord(), false);
            minecraftConfigData.setMessage("kickMessage", getKickMessage(), false);
        }
    }
}

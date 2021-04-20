package com.github.jenya705.connect;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class ConnectConfig implements DiscomcConfig {

    private boolean enabled = true;
    private long connectChannelID = -1;
    private int maxCodeValue = 10000;
    private int codeRemoveTime = 6000;
    private long discordMessageDeleteTime = 300;
    private String alreadyConnected = "You are already connected with &b{0}&r";
    private String connectRequest = "Your code is &b{0}&r write it in channel &b{1}&r";
    private String codeTimeOver = "&cTime is over, your code is cleared";
    private String successfullyConnectedMinecraft = "You connected with &b{0}&r";
    private String successfullyConnectedDiscord = "{0}, success! You are connected!";
    private String codeNotExist = "{0}, this code is not exist";

    @Override
    public void load(SerializedData data) {
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            setAlreadyConnected(minecraftConfigData.getMessage("alreadyConnected", getAlreadyConnected(), true));
            setConnectRequest(minecraftConfigData.getMessage("connectRequest", getConnectRequest(), true));
            setCodeTimeOver(minecraftConfigData.getMessage("codeTimeOver", getCodeTimeOver(), true));
            setSuccessfullyConnectedMinecraft(minecraftConfigData.getMessage(
                    "successfullyConnectedMinecraft", getSuccessfullyConnectedMinecraft(), true));
            setSuccessfullyConnectedDiscord(minecraftConfigData.getMessage(
                    "successfullyConnectedDiscord", getSuccessfullyConnectedDiscord(), false));
            setCodeNotExist(minecraftConfigData.getMessage("codeNotExist", getCodeNotExist(), false));
        }
        setEnabled(data.getBoolean("enabled", isEnabled()));
        setConnectChannelID(data.getLong("connectChannelID", getConnectChannelID()));
        setMaxCodeValue(data.getInteger("maxCodeValue", getMaxCodeValue()));
        setCodeRemoveTime(data.getInteger("codeRemoveTime", getCodeRemoveTime()));
        setDiscordMessageDeleteTime(data.getLong("discordMessageDeleteTime", getDiscordMessageDeleteTime()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("enabled", isEnabled());
        data.setObject("connectChannelID", getConnectChannelID());
        data.setObject("maxCodeValue", getMaxCodeValue());
        data.setObject("codeRemoveTime", getCodeRemoveTime());
        data.setObject("discordMessageDeleteTime", getDiscordMessageDeleteTime());
        if (data instanceof MinecraftConfigData) {
            MinecraftConfigData minecraftConfigData = (MinecraftConfigData) data;
            minecraftConfigData.setMessage("alreadyConnected", getAlreadyConnected(), true);
            minecraftConfigData.setMessage("connectRequest", getConnectRequest(), true);
            minecraftConfigData.setMessage("codeTimeOver", getCodeTimeOver(), true);
            minecraftConfigData.setMessage("successfullyConnectedMinecraft", getSuccessfullyConnectedMinecraft(), true);
            minecraftConfigData.setMessage("successfullyConnectedDiscord", getSuccessfullyConnectedDiscord(), false);
            minecraftConfigData.setMessage("codeNotExist", getCodeNotExist(), false);
        }
    }
}

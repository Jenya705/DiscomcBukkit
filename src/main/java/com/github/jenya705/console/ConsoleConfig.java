package com.github.jenya705.console;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class ConsoleConfig implements DiscomcConfig {

    private boolean enabled = true;
    private long consoleChannelID = -1;
    private int messagesSendingTimer = 20;

    @Override
    public void load(SerializedData data) {
        setEnabled(data.getBoolean("enabled", isEnabled()));
        setConsoleChannelID(data.getLong("consoleChannelID", getConsoleChannelID()));
        setMessagesSendingTimer(data.getInteger("messagesSendingTimer", getMessagesSendingTimer()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("enabled", isEnabled());
        data.setObject("consoleChannelID", getConsoleChannelID());
        data.setObject("messagesSendingTimer", getMessagesSendingTimer());
    }
}

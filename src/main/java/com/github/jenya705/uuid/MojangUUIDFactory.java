package com.github.jenya705.uuid;

import com.github.jenya705.Discomc;
import me.kbrewster.mojangapi.MojangAPI;

import java.util.UUID;
import java.util.logging.Level;

public class MojangUUIDFactory implements UUIDFactory{

    @Override
    public UUID getUUID(String nickname) {
        try {
            return MojangAPI.getUUID(nickname);
        } catch (Exception e) {
            Discomc.getPlugin().getLogger().log(Level.WARNING, "[MojangAPI] Exception:", e);
        }
        return null;
    }
}

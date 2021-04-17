package com.github.jenya705.uuid;

import com.github.jenya705.Discomc;
import org.shanerx.mojang.Mojang;

import java.util.UUID;

public class MojangUUIDFactory implements UUIDFactory{

    private static final Mojang api = new Mojang().connect();

    @Override
    public UUID getUUID(String nickname) {
        if (api.getStatus(Mojang.ServiceType.API_MOJANG_COM) != Mojang.ServiceStatus.GREEN) {
            Discomc.getPlugin().getLogger().warning("[MojangAPI] api.mojang.com service status is not green");
            return null;
        }
        String uuid = api.getUUIDOfUsername(nickname);
        if (uuid != null) {
            return UUID.fromString(uuid.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                    "$1-$2-$3-$4-$5"));
        }
        return null;
    }
}

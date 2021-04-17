package com.github.jenya705.skin;

import java.util.UUID;

public class CrafatarHeadURLFactory implements HeadURLFactory{

    private static final String crafatar_url = "http://crafatar.com/avatars/%s?overlay=true";
    private static final String default_crafatar_url = String.format(crafatar_url, "c06f89064c8a49119c29ea1dbd1aab82");

    @Override
    public String getHeadURL(UUID uuid) {
        if (uuid == null) {
            return default_crafatar_url;
        }
        return String.format(crafatar_url, uuid.toString());
    }
}

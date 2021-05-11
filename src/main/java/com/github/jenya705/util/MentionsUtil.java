package com.github.jenya705.util;

import discord4j.common.util.Snowflake;
import org.apache.commons.lang.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MentionsUtil {

    public static final Pattern USER_MENTION_PATTERN = Pattern.compile("<@(|!)(\\d{1,19})>");
    public static final Pattern ROLE_MENTION_PATTERN = Pattern.compile("<@&(\\d{1,19})>");
    public static final Pattern CHANNEL_MENTION_PATTERN = Pattern.compile("<#(\\d{1,19})>");
    public static final Pattern MENTIONABLE_MENTION_PATTERN = Pattern.compile("<@(|!|&)(\\d{1,19})>");

    /**
     * @param raw raw message
     * @param mentionPattern mention pattern
     * @param checkForNumber if raw is number it will convert to snowflake
     * @param numGroup group of snowflake id
     * @return not null Snowflake is success
     */
    public static Snowflake snowflakeFromRaw(String raw, Pattern mentionPattern, boolean checkForNumber, int numGroup) {
        Matcher matcher = mentionPattern.matcher(raw);
        if (matcher.find()) {
            return Snowflake.of(matcher.group(numGroup));
        }
        if (checkForNumber && NumberUtils.isNumber(raw)) {
            return Snowflake.of(raw);
        }
        return null;
    }

}

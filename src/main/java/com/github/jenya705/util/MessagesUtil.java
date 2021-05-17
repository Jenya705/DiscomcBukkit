package com.github.jenya705.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;

@UtilityClass
public class MessagesUtil {

    public static String serializePlainMessage(String message) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); ++i) {
            char currentChar = message.charAt(i);
            switch (currentChar){
                case '*':
                case '|':
                case '~':
                case '_':
                case '`':
                    builder.append('\\').append(currentChar);
                    break;
                case '&':
                case ChatColor.COLOR_CHAR:
                    i++;
                    break;
                default:
                    builder.append(currentChar);
                    break;
            }
        }
        return builder.toString();
    }

    public static <T> String listToString(List<T> list) {
        StringBuilder builder = new StringBuilder();
        for (T value: list) {
            builder.append(value.toString()).append(" ");
        }
        return builder.toString();
    }

}

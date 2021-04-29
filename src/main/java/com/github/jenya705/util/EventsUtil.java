package com.github.jenya705.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@UtilityClass
public class EventsUtil {

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

}

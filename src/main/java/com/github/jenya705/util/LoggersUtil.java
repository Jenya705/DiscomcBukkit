package com.github.jenya705.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoggersUtil {

    public static java.util.logging.Level toJUL(org.apache.logging.log4j.Level level) {
        if (level == org.apache.logging.log4j.Level.ERROR) return java.util.logging.Level.SEVERE;
        if (level == org.apache.logging.log4j.Level.WARN) return java.util.logging.Level.WARNING;
        if (level == org.apache.logging.log4j.Level.DEBUG) return java.util.logging.Level.FINE;
        return java.util.logging.Level.INFO;
    }

}

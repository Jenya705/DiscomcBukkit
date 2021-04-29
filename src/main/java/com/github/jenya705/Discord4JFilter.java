package com.github.jenya705;

import com.github.jenya705.util.LoggersUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class Discord4JFilter implements Filter {

    @Override
    public Result getOnMismatch() {
        return Result.NEUTRAL;
    }

    @Override
    public Result getOnMatch() {
        return Result.NEUTRAL;
    }

    public Result filter(String loggerName, Level level, String message, Throwable throwable) {
        if (!loggerName.startsWith("discord4j")) return Result.NEUTRAL;
        Discomc discomc = Discomc.getPlugin();
        if (throwable != null) {
            if (level.intLevel() > Level.WARN.intLevel()) {
                discomc.getLogger().log(LoggersUtil.toJUL(level), "[Discord4J] " + message, throwable);
            }
        }
        else {
            if (level.intLevel() > Level.WARN.intLevel()) {
                discomc.getLogger().log(LoggersUtil.toJUL(level), "[Discord4J] " + message);
            }
        }
        return Result.DENY;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return filter(logger.getName(), level, msg, null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return filter(logger.getName(), level, String.valueOf(msg), t);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return filter(logger.getName(), level, msg.getFormattedMessage(), t);
    }

    @Override
    public Result filter(LogEvent event) {
        return filter(event.getLoggerName(), event.getLevel(), event.getMessage().getFormattedMessage(), event.getThrown());
    }

    @Override
    public void start() { /* NOTHING */ }

    @Override
    public void stop() { /* NOTHING */ }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public boolean isStopped() {
        return false;
    }
}

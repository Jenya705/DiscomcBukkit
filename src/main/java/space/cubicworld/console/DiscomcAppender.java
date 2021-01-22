package space.cubicworld.console;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.bukkit.scheduler.BukkitTask;
import space.cubicworld.DiscomcPlugin;

public class DiscomcAppender extends AbstractAppender {

    private static BukkitTask discomcConsoleMessagesSender;

    public static final PatternLayout PATTERN_LAYOUT = PatternLayout.newBuilder()
            .withPattern("[%d{HH:mm:ss} %p] %m")
            .build();

    public DiscomcAppender(String name) {
        super(name, null, null);
        DiscomcPlugin.addBukkitTask(new DiscomcConsoleMessagesSender().runTaskAsynchronously(DiscomcPlugin.getInstance()));
        start();
    }

    @Override
    public void append(LogEvent logEvent) {
        DiscomcConsoleMessagesSender.addMessage(PATTERN_LAYOUT.toSerializable(logEvent));
    }

    public static BukkitTask getDiscomcConsoleMessagesSender(){
        return discomcConsoleMessagesSender;
    }

}

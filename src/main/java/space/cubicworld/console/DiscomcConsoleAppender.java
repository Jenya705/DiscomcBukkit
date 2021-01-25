package space.cubicworld.console;

import ch.jalu.configme.SettingsManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import space.cubicworld.DiscomcConfiguration;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.service.DiscomcConsoleMessagesSender;

public class DiscomcConsoleAppender extends AbstractAppender {

    public static final PatternLayout PATTERN_LAYOUT = PatternLayout.newBuilder()
            .withPattern("[%d{HH:mm:ss} %p] %m")
            .build();

    public DiscomcConsoleAppender(String name) {
        super(name, null, null);
        SettingsManager discomcConfig = DiscomcPlugin.getInstance().getDiscomcConfig();
        long consoleUpdate = discomcConfig.getProperty(DiscomcConfiguration.CONSOLE_UPDATE);
        DiscomcPlugin.addBukkitTask(new DiscomcConsoleMessagesSender().runTaskTimerAsynchronously(
                DiscomcPlugin.getInstance(), consoleUpdate, consoleUpdate));
        start();
    }

    @Override
    public void append(LogEvent logEvent) {
        DiscomcConsoleMessagesSender.addMessage(PATTERN_LAYOUT.toSerializable(logEvent));
    }

}

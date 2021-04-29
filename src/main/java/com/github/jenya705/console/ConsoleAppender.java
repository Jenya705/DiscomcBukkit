package com.github.jenya705.console;

import com.github.jenya705.Discomc;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class ConsoleAppender extends AbstractAppender {

    private static final PatternLayout defaultPatternLayout = PatternLayout.newBuilder()
            .withPattern("[%d{HH:mm:ss} %level]: %msg")
            .build();

    protected ConsoleAppender() {
        super("discomc-console-appender", null, defaultPatternLayout, false);
        start();
    }

    @Override
    public void append(LogEvent event) {
        Discomc discomc = Discomc.getPlugin();
        ConsoleModule consoleModule = discomc.getConsoleModule();
        consoleModule.getConsoleSenderService().getMessagesDeque()
                .add(defaultPatternLayout.toSerializable(event));
    }

}

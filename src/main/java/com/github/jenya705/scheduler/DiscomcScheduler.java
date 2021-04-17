package com.github.jenya705.scheduler;

public interface DiscomcScheduler {

    DiscomcScheduledTask runTask(Runnable runnable);

    DiscomcScheduledTask runTaskLater(Runnable runnable, int time);

    DiscomcScheduledTask runTaskTimer(Runnable runnable, int timer);

    DiscomcScheduledTask runTaskTimer(Runnable runnable, int start, int timer);

    DiscomcScheduledTask runTaskAsynchronously(Runnable runnable);

    DiscomcScheduledTask runTaskLaterAsynchronously(Runnable runnable, int time);

    DiscomcScheduledTask runTaskTimerAsynchronously(Runnable runnable, int timer);

    DiscomcScheduledTask runTaskTimerAsynchronously(Runnable runnable, int start, int timer);

}

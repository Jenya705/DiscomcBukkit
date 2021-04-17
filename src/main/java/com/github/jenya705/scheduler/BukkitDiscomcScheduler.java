package com.github.jenya705.scheduler;

import com.github.jenya705.Discomc;

public class BukkitDiscomcScheduler implements DiscomcScheduler{

    @Override
    public DiscomcScheduledTask runTask(Runnable runnable) {
        Discomc discomc = Discomc.getPlugin();
        return new BukkitDiscomcScheduledTask(discomc.getServer()
                .getScheduler().runTask(discomc, runnable));
    }

    @Override
    public DiscomcScheduledTask runTaskLater(Runnable runnable, int time) {
        Discomc discomc = Discomc.getPlugin();
        return new BukkitDiscomcScheduledTask(discomc.getServer()
                .getScheduler().runTaskLater(discomc, runnable, time));
    }

    @Override
    public DiscomcScheduledTask runTaskTimer(Runnable runnable, int timer) {
        Discomc discomc = Discomc.getPlugin();
        return new BukkitDiscomcScheduledTask(discomc.getServer()
                .getScheduler().runTaskTimer(discomc, runnable, timer, timer));
    }

    @Override
    public DiscomcScheduledTask runTaskTimer(Runnable runnable, int start, int timer) {
        Discomc discomc = Discomc.getPlugin();
        return new BukkitDiscomcScheduledTask(discomc.getServer()
                .getScheduler().runTaskTimer(discomc, runnable, start, timer));
    }

    @Override
    public DiscomcScheduledTask runTaskAsynchronously(Runnable runnable) {
        Discomc discomc = Discomc.getPlugin();
        return new BukkitDiscomcScheduledTask(discomc.getServer()
                .getScheduler().runTaskAsynchronously(discomc, runnable));
    }

    @Override
    public DiscomcScheduledTask runTaskLaterAsynchronously(Runnable runnable, int time) {
        Discomc discomc = Discomc.getPlugin();
        return new BukkitDiscomcScheduledTask(discomc.getServer()
                .getScheduler().runTaskLaterAsynchronously(discomc, runnable, time));
    }

    @Override
    public DiscomcScheduledTask runTaskTimerAsynchronously(Runnable runnable, int timer) {
        Discomc discomc = Discomc.getPlugin();
        return new BukkitDiscomcScheduledTask(discomc.getServer()
                .getScheduler().runTaskTimerAsynchronously(discomc, runnable, timer, timer));
    }

    @Override
    public DiscomcScheduledTask runTaskTimerAsynchronously(Runnable runnable, int start, int timer) {
        Discomc discomc = Discomc.getPlugin();
        return new BukkitDiscomcScheduledTask(discomc.getServer()
                .getScheduler().runTaskTimerAsynchronously(discomc, runnable, start, timer));
    }
}

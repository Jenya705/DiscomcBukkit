package com.github.jenya705.scheduler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitTask;

@Getter
@Setter(AccessLevel.PROTECTED)
public class BukkitDiscomcScheduledTask implements DiscomcScheduledTask {

    private BukkitTask bukkitTask;

    public BukkitDiscomcScheduledTask(BukkitTask bukkitTask) {
        setBukkitTask(bukkitTask);
    }

    @Override
    public void cancel() {
        bukkitTask.cancel();
    }
}

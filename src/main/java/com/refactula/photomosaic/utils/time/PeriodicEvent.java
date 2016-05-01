package com.refactula.photomosaic.utils.time;

import java.util.concurrent.TimeUnit;

public class PeriodicEvent {

    private final long triggerInterval;

    private long lastTriggerTime = 0;

    public PeriodicEvent(long interval, TimeUnit timeUnit) {
        triggerInterval = timeUnit.toMillis(interval);
    }

    public boolean update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < lastTriggerTime + triggerInterval) {
            return false;
        }
        lastTriggerTime = currentTime;
        return true;
    }

}

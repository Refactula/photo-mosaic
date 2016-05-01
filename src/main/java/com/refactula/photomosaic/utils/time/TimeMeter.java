package com.refactula.photomosaic.utils.time;

public class TimeMeter {

    private long startTime;
    private Long stopTime = null;

    public TimeMeter(long startTime) {
        this.startTime = startTime;
    }

    public static TimeMeter start() {
        return new TimeMeter(System.currentTimeMillis());
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
    }

    public long get() {
        return (stopTime != null ? stopTime : System.currentTimeMillis()) - startTime;
    }

}

package com.banuh.frologue.core.utils;

public class Schedule {
    public long time;
    public long startTime;
    public long endTime;
    private Runnable callback;

    public Schedule(long startTime, long time, Runnable callback) {
        this.time = time;
        this.startTime = startTime;
        this.endTime = startTime + time;
        this.callback = callback;
    }

    public void apply() {
        callback.run();
    }
}

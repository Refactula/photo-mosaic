package com.refactula.photomosaic.utils.time;

/**
 * This class uses spend time and task progress to estimate in real time remaining time
 */
public class ProgressEstimator {

    private TimeMeter timeMeter;

    public ProgressEstimator(TimeMeter timeMeter) {
        if (timeMeter == null) {
            throw new NullPointerException();
        }
        this.timeMeter = timeMeter;
    }

    public long estimateRemainingTime(double progress) {
        if (progress < 1.0E-06) {
            return Long.MAX_VALUE; // Avoids division by zero
        }

        progress = Math.min(Math.max(0.0, progress), 1.0);
        long spendTime = timeMeter.get();
        double speed = progress / spendTime;
        double remainingProgress = 1.0 - progress;
        return Math.round(remainingProgress / speed);
    }

}

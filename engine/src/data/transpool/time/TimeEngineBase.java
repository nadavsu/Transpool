package data.transpool.time;

import data.transpool.Updatable;
import data.transpool.time.component.TimeDay;
import data.transpool.time.component.TimeInterval;

public class TimeEngineBase implements TimeEngine {
    public static TimeDay currentTime;

    public TimeEngineBase() {
        currentTime = new TimeDay();
    }

    @Override
    public void incrementTime(TimeInterval interval, Updatable updatable) {
        currentTime.plus(interval.getMinutes());
        updatable.update();
    }

    @Override
    public void decrementTime(TimeInterval interval, Updatable updatable) {
        currentTime.minus(interval.getMinutes());
        updatable.update();
    }

    @Override
    public void incrementTime(TimeInterval interval) {
        currentTime.plus(interval.getMinutes());
    }

    @Override
    public void decrementTime(TimeInterval interval) {
        currentTime.minus(interval.getMinutes());
    }

    @Override
    public TimeDay getCurrentTime() {
        return currentTime;
    }
}
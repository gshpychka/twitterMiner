public class Timeframe {
    private long startTime;
    private long endTime;
    private int period;
    Timeframe(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.period = (int)(endTime - startTime);
    }

    Timeframe(long startTime, int period) {
        this(startTime,startTime+period);
    }

    long getStartTime() {
        return startTime;
    }

    void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    long getEndTime() {
        return endTime;
    }

    void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    int getPeriod() {
        return period;
    }

    void setPeriod(int period) {
        this.period = period;
    }
}

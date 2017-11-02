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
        this.endTime = startTime + this.period;
    }

    long getEndTime() {
        return this.endTime;
    }

    void setEndTime(long endTime) {
        this.endTime = endTime;
        this.startTime = endTime - this.period;
    }

    int getPeriod() {
        return this.period;
    }

    void setPeriod(int period) {
        this.period = period;
        this.endTime = this.startTime + period;
    }
}

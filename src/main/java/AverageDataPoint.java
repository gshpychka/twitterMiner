import java.math.BigDecimal;

/**
 * Created by glebu on 16-Feb-17.
 */
class AverageDataPoint {
    private Timeframe timeframe;
    private BigDecimal dataPoint;
    private String keyword;
    private long startTime;
    private int period;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    String getKeyword() {
        return keyword;
    }

    void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    void setDataPoint(BigDecimal dataPoint) {
        this.dataPoint = dataPoint;
    }

    long getStartTime() {

        return startTime;
    }

    BigDecimal getDataPoint() {
        return dataPoint;
    }

    AverageDataPoint(Timeframe timeframe, BigDecimal dataPoint, String keyword) {
        this.timeframe = timeframe;
        this.dataPoint = dataPoint;
        this.keyword = keyword;
        this.startTime = timeframe.getStartTime();
        this.period = timeframe.getPeriod();
    }
}

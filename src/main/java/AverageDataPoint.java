import java.math.BigDecimal;

/**
 * Created by glebu on 16-Feb-17.
 */
public class AverageDataPoint {
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setDataPoint(BigDecimal dataPoint) {
        this.dataPoint = dataPoint;
    }

    public long getStartTime() {

        return startTime;
    }

    void setTimeframe(Timeframe timeframe) {
        this.timeframe = timeframe;
        this.startTime = timeframe.getStartTime();
        this.period = timeframe.getPeriod();
    }

    public BigDecimal getDataPoint() {
        return dataPoint;
    }

    public AverageDataPoint() {

    }

    AverageDataPoint(Timeframe timeframe, BigDecimal dataPoint, String keyword) {
        this.timeframe = timeframe;
        this.dataPoint = dataPoint;
        this.keyword = keyword;
        this.startTime = timeframe.getStartTime();
        this.period = timeframe.getPeriod();

    }
}

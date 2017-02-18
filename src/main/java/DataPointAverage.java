import java.math.BigDecimal;

/**
 * Created by glebu on 16-Feb-17.
 */
class DataPointAverage {
    private long startTime;
    private BigDecimal dataPoint;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    DataPointAverage(long startTime, BigDecimal dataPoint) {
        this.startTime = startTime;
        this.dataPoint = dataPoint;

    }
}

import java.math.BigDecimal;

/**
 * Created by glebu on 16-Feb-17.
 */
class DataPointAverage {
    private long startTime;
    private BigDecimal dataPoint;
    private int id;
    private String keyword;

    String getKeyword() {
        return keyword;
    }

    void setKeyword(String keyword) {
        this.keyword = keyword;
    }

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

    DataPointAverage(long startTime, BigDecimal dataPoint, String keyword) {
        this.startTime = startTime;
        this.dataPoint = dataPoint;
        this.keyword = keyword;

    }
}

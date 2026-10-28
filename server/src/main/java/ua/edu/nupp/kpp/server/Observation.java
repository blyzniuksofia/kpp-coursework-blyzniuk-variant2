package ua.edu.nupp.kpp.server;

public class Observation {
    private Long id;
    private long locationId;
    private String indicator;
    private double observedValue;
    private double limitValue;
    private double scaleScore;
    private double durationScore;
    private String observedDate;

    public Observation(){
    }

    public Observation(Long id,
                       long locationId,
                       String indicator,
                       double observedValue,
                       double limitValue,
                       double scaleScore,
                       double durationScore,
                       String observedDate){
        this.id = id;
        this.locationId = locationId;
        this.indicator = indicator;
        this.observedValue = observedValue;
        this.limitValue = limitValue;
        this.scaleScore = scaleScore;
        this.durationScore = durationScore;
        this.observedDate = observedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public double getObservedValue() {
        return observedValue;
    }

    public void setObservedValue(double observedValue) {
        this.observedValue = observedValue;
    }

    public double getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(double limitValue) {
        this.limitValue = limitValue;
    }

    public double getScaleScore() {
        return scaleScore;
    }

    public void setScaleScore(double scaleScore) {
        this.scaleScore = scaleScore;
    }

    public double getDurationScore() {
        return durationScore;
    }

    public void setDurationScore(double durationScore) {
        this.durationScore = durationScore;
    }

    public String getObservedDate() {
        return observedDate;
    }

    public void setObservedDate(String observedDate) {
        this.observedDate = observedDate;
    }

}

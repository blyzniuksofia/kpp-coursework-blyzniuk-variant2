package ua.edu.nupp.kpp.server;

public class Incident {
    private Long id;
    private long observationId;
    private IncidentStatus status;
    private double f1;
    private double f2;
    private double f3;
    private double criticalIndex;

    public Incident(){
    }

    public Incident(Long id,
                    long observationId,
                    IncidentStatus status,
                    double f1,
                    double f2,
                    double f3,
                    double criticalIndex){
        this.id = id;
        this.observationId = observationId;
        this.status = status;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.criticalIndex = criticalIndex;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getObservationId() {
        return observationId;
    }

    public void setObservationId(long observationId) {
        this.observationId = observationId;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public double getF1() {
        return f1;
    }

    public void setF1(double f1) {
        this.f1 = f1;
    }

    public double getF2() {
        return f2;
    }

    public void setF2(double f2) {
        this.f2 = f2;
    }

    public double getF3() {
        return f3;
    }

    public void setF3(double f3) {
        this.f3 = f3;
    }

    public double getCriticalIndex() {
        return criticalIndex;
    }

    public void setCriticalIndex(double criticalIndex) {
        this.criticalIndex = criticalIndex;
    }
}

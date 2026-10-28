package ua.edu.nupp.kpp.server;

public class ResponseMeasure {
    private Long id;
    private long incidentId;
    private String description;
    private String plannedDate;

    public ResponseMeasure() {
    }

    public ResponseMeasure(Long id,
                           long incidentId,
                           String description,
                           String plannedDate) {
        this.id = id;
        this.incidentId = incidentId;
        this.description = description;
        this.plannedDate = plannedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(long incidentId) {
        this.incidentId = incidentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(String plannedDate) {
        this.plannedDate = plannedDate;
    }
}

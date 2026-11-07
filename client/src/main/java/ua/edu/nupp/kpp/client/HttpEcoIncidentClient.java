package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


public class HttpEcoIncidentClient {
    private static final String BASE="http://localhost:8080/eco-incident-server";
    private final HttpClient http=HttpClient.newHttpClient();
    private final ObjectMapper mapper=new ObjectMapper();

    public List<Location> getLocations() {
        return Arrays.asList(read(get("/locations"),Location[].class));
    }
    public void addLocation(Location l) {
        post("/locations", "action=add&code="+enc(l.getCode())+"&name="
                +enc(l.getName())+"&city="+enc(l.getCity()));
    }
    public void updateLocation(Location l) {
        post("/locations", "action=update&id="+l.getId()+"&code="
                +enc(l.getCode())+"&name="+enc(l.getName())+"&city="+enc(l.getCity()));
    }
    public void deleteLocation(long id) {
        post("/locations", "action=delete&id="+id);
    }

    public List<Observation> getObservations() {
        return Arrays.asList(read(get("/observations"),Observation[].class));
    }
    public void addObservation(Observation o) {
        post("/observations", observationForm("add",o));
    }
    public void updateObservation(Observation o) {
        post("/observations",observationForm("update",o)+"&id="+o.getId());
    }
    public void deleteObservation(long id) {
        post("/observations","action=delete&id="+id);
    }

    public List<Incident> getIncidents() {
        return Arrays.asList(read(get("/incidents"), Incident[].class));
    }
    public void addIncident(long observationId) {
        post("/incidents","action=add&observationId="+observationId);
    }
    public void updateIncident(long id,long observationId) {
        post("/incidents","action=update&id="+id+"&observationId="+observationId);
    }

    public void changeIncidentStatus(long id,IncidentStatus status) {
        post("/incidents","action=transition&id="+id+"&status="+status.name());
    }
    public void deleteIncident(long id) {
        post("/incidents", "action=delete&id=" + id);
    }

    public List<ResponseMeasure> getResponses() {
        return Arrays.asList(read(get("/responses"),ResponseMeasure[].class));
    }
    public void addResponse(ResponseMeasure r) {
        post("/responses",responseForm("add",r));
    }
    public void updateResponse(ResponseMeasure r) {
        post("/responses",responseForm("update",r)+"&id="+r.getId());
    }
    public void deleteResponse(long id) {
        post("/responses", "action=delete&id=" + id);
    }

    private String observationForm(String action,Observation o) {
        return "action="+action+"&locationId="+o.getLocationId()+"&indicator="+enc(o.getIndicator())+
                "&observedValue="+o.getObservedValue()+"&limitValue="+o.getLimitValue()+
                "&scaleScore="+o.getScaleScore()+"&durationScore="+o.getDurationScore()+"&observedDate=" + enc(o.getObservedDate());
    }
    private String responseForm(String action,ResponseMeasure r) {
        return "action="+action+"&incidentId="+r.getIncidentId()+"&description="+enc(r.getDescription())
                +"&plannedDate="+enc(r.getPlannedDate());
    }

    private String get(String path) {
        HttpRequest req=HttpRequest.newBuilder(URI.create(BASE+path)).GET().build();
        return send(req);
    }

    private String send(HttpRequest req) {
        try {
            HttpResponse<String> r=http.send(req,HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if(r.statusCode()>=400) throw new RuntimeException(r.body());
            return r.body();
        } catch(Exception e) { throw new RuntimeException("Помилка HTTP: "+e.getMessage(),e); }
    }

    private String post(String path,String form) {
        HttpRequest req=HttpRequest.newBuilder(URI.create(BASE+path))
                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(form)).build(); return send(req);
    }

    private <T> T read(String json,Class<T> type) {
        try { return mapper.readValue(json,type);
        } catch(Exception e) { throw new RuntimeException("Помилка JSON",e); }
    }

    private String enc(String s) {
        return URLEncoder.encode(s==null?"":s,StandardCharsets.UTF_8);
    }
}

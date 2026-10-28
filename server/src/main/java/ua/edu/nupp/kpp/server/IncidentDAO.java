package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidentDAO {
    public void add(Incident i) throws SQLException {
        String sql = "INSERT INTO incident(observation_id,status,f1,f2,f3,critical_index) VALUES(?,?,?,?,?,?)";
        try (Connection c=DBUtil.getConnection();
             PreparedStatement p=c.prepareStatement(sql)) {
            bind(p,i);
            p.executeUpdate();
        }
    }
    public List<Incident> getAll() throws SQLException {
        List<Incident> list=new ArrayList<>();
        try (Connection c=DBUtil.getConnection();
             PreparedStatement p=c.prepareStatement("SELECT * FROM incident ORDER BY id");
             ResultSet r=p.executeQuery()) {
            while(r.next())
                list.add(map(r));
        }
        return list;
    }
    public Incident getById(long id) throws SQLException {
        try (Connection c=DBUtil.getConnection();
             PreparedStatement p=c.prepareStatement("SELECT * FROM incident WHERE id=?")) {
            p.setLong(1,id);
            try(ResultSet r=p.executeQuery()) {
                return r.next()?map(r):null;
            }
        }
    }
    public void update(Incident i) throws SQLException {
        String sql="UPDATE incident SET observation_id=?,status=?,f1=?,f2=?,f3=?,critical_index=? WHERE id=?";
        try(Connection c=DBUtil.getConnection();
            PreparedStatement p=c.prepareStatement(sql)) {
            bind(p,i);
            p.setLong(7,i.getId());
            p.executeUpdate();
        }
    }
    public void updateStatus(long id, IncidentStatus status) throws SQLException {
        try(Connection c=DBUtil.getConnection();
            PreparedStatement p=c.prepareStatement("UPDATE incident SET status=? WHERE id=?")) {
            p.setString(1, status.name());
            p.setLong(2,id);
            p.executeUpdate();
        }
    }
    public void delete(long id) throws SQLException {
        try(Connection c=DBUtil.getConnection();
            PreparedStatement p=c.prepareStatement("DELETE FROM incident WHERE id=?")) {
            p.setLong(1,id);
            p.executeUpdate();
        }
    }
    private void bind(PreparedStatement p, Incident i) throws SQLException {
        p.setLong(1,i.getObservationId());
        p.setString(2,i.getStatus().name());
        p.setDouble(3,i.getF1());
        p.setDouble(4,i.getF2());
        p.setDouble(5,i.getF3());
        p.setDouble(6,i.getCriticalIndex());
    }
    private Incident map(ResultSet r) throws SQLException {
        return new Incident(
                r.getLong("id"),
                r.getLong("observation_id"),IncidentStatus.valueOf(r.getString("status")),
                r.getDouble("f1"),
                r.getDouble("f2"),
                r.getDouble("f3"),
                r.getDouble("critical_index"));
    }

}

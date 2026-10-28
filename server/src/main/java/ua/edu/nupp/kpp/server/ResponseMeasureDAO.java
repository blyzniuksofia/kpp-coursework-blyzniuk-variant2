package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResponseMeasureDAO {
    public void add(ResponseMeasure m) throws SQLException {
        String sql="INSERT INTO response_measure(incident_id,description,planned_date) VALUES(?,?,?)";
        try(Connection c=DBUtil.getConnection();
            PreparedStatement p=c.prepareStatement(sql)) {
            bind(p,m);
            p.executeUpdate();
        }
    }
    public List<ResponseMeasure> getAll() throws SQLException {
        List<ResponseMeasure> list=new ArrayList<>();
        try(Connection c=DBUtil.getConnection();
            PreparedStatement p=c.prepareStatement("SELECT * FROM response_measure ORDER BY id");
            ResultSet r=p.executeQuery()) {
            while(r.next())
                list.add(map(r));
        }
        return list;
    }
    public ResponseMeasure getById(long id) throws SQLException {
        try(Connection c=DBUtil.getConnection();
            PreparedStatement p=c.prepareStatement("SELECT * FROM response_measure WHERE id=?")) {
            p.setLong(1,id);
            try(ResultSet r=p.executeQuery()) {
                return r.next()?map(r):null;
            }
        }
    }
    public void update(ResponseMeasure m) throws SQLException {
        String sql="UPDATE response_measure SET incident_id=?,description=?,planned_date=? WHERE id=?";
        try(Connection c=DBUtil.getConnection();
            PreparedStatement p=c.prepareStatement(sql)) {
            bind(p,m);
            p.setLong(4,m.getId());
            p.executeUpdate();
        }
    }
    public void delete(long id) throws SQLException {
        try(Connection c=DBUtil.getConnection();
            PreparedStatement p=c.prepareStatement("DELETE FROM response_measure WHERE id=?")) {
            p.setLong(1,id);
            p.executeUpdate();
        }
    }
    private void bind(PreparedStatement p, ResponseMeasure m) throws SQLException {
        p.setLong(1,m.getIncidentId());
        p.setString(2,m.getDescription());
        p.setString(3,m.getPlannedDate());
    }
    private ResponseMeasure map(ResultSet r) throws SQLException {
        return new ResponseMeasure(
                r.getLong("id"),
                r.getLong("incident_id"),
                r.getString("description"),
                r.getString("planned_date"));
    }

}

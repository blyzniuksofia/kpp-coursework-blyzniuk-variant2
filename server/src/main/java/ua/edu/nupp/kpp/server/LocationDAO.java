package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {
    public void add(Location l) throws SQLException {
        String sql = "INSERT INTO location (code,name,city) VALUES(?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, l.getCode());
            p.setString(2, l.getName());
            p.setString(3, l.getCity());
            p.executeUpdate();
        }
    }
    public List<Location> getAll() throws SQLException {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT id,code,name,city FROM location ORDER BY id";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next())
                list.add(new Location(
                        r.getLong("id"),
                        r.getString("code"),
                        r.getString("name"),
                        r.getString("city")));
        }
        return list;
    }
    public Location getById(long id) throws SQLException {
        String sql = "SELECT id,code,name,city FROM location WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? new Location(
                        r.getLong("id"),
                        r.getString("code"),
                        r.getString("name"),
                        r.getString("city")) : null;
            }
        }
    }
    public void update(Location l) throws SQLException {
        String sql = "UPDATE location SET code=?,name=?,city=? WHERE id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, l.getCode());
            p.setString(2, l.getName());
            p.setString(3, l.getCity());
            p.setLong(4, l.getId());
            p.executeUpdate();
        }
    }
    public void delete(long id) throws SQLException {
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement("DELETE FROM location WHERE id=?")) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}

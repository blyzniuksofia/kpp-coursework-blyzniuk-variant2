package ua.edu.nupp.kpp.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObservationDAO {
        public void add(Observation o) throws SQLException {
            String sql = "INSERT INTO observation(location_id,indicator,observed_value,limit_value," +
                    "scale_score,duration_score, observed_date) VALUES(?,?,?,?,?,?,?)";
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement p = c.prepareStatement(sql)) {
                bind(p, o);
                p.executeUpdate();
            }
        }

        public List<Observation> getAll() throws SQLException {
            List<Observation> list = new ArrayList<>();
            String sql = "SELECT * FROM observation ORDER BY id";
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement p = c.prepareStatement(sql);
                 ResultSet r = p.executeQuery()) {
                while (r.next())
                    list.add(map(r));
            }
            return list;
        }

        public Observation getById(long id) throws SQLException {
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement p = c.prepareStatement("SELECT * FROM observation WHERE id=?")) {
                p.setLong(1, id);
                try (ResultSet r = p.executeQuery()) {
                    return r.next() ? map(r) : null;
                }
            }
        }

        public void update(Observation o) throws SQLException {
            String sql = "UPDATE observation SET location_id=?,indicator=?,observed_value=?,limit_value=?," +
                    "scale_score=?, duration_score=?,observed_date=? WHERE id=?";
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement p = c.prepareStatement(sql)) {
                bind(p, o);
                p.setLong(8, o.getId());
                p.executeUpdate();
            }
        }

        public void delete(long id) throws SQLException {
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement p = c.prepareStatement("DELETE FROM observation WHERE id=?")) {
                p.setLong(1, id);
                p.executeUpdate();
            }
        }

        private void bind(PreparedStatement p, Observation o) throws SQLException {
            p.setLong(1, o.getLocationId());
            p.setString(2, o.getIndicator());
            p.setDouble(3, o.getObservedValue());
            p.setDouble(4, o.getLimitValue());
            p.setDouble(5, o.getScaleScore());
            p.setDouble(6, o.getDurationScore());
            p.setString(7, o.getObservedDate());
        }

        private Observation map(ResultSet r) throws SQLException {
            return new Observation(
                    r.getLong("id"),
                    r.getLong("location_id"),
                    r.getString("indicator"),
                    r.getDouble("observed_value"),
                    r.getDouble("limit_value"),
                    r.getDouble("scale_score"),
                    r.getDouble("duration_score"),
                    r.getString("observed_date")
            );
        }
}

package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class LocationService {
    private static final Logger LOG = Logger.getLogger(Location.class.getName());

    private final LocationDAO dao = new LocationDAO();
    public List<Location> getAll() throws SQLException {
        return dao.getAll();
    }

    public void add(Location l) throws SQLException {
        validate(l);
        dao.add(l);
        LOG.info("Додано локацію.");
    }

    public void update(Location l) throws SQLException {
        validate(l);
        dao.update(l);
        LOG.info("Оновлено локацію.");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено локацію.");
    }

    static void validate(Location l) {
        if(l==null || blank(l.getCode()) || blank(l.getName()) || blank(l.getCity()))
            throw new IllegalArgumentException("Усі поля локації повинні бути заповнені.");
    }
    private static boolean blank(String v) {
        return v==null || v.isBlank();
    }

}

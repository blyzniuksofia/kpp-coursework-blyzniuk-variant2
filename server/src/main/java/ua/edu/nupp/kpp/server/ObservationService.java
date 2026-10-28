package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ObservationService {
    private static final Logger LOG =
            Logger.getLogger(ObservationService.class.getName());

    private final ObservationDAO dao = new ObservationDAO();
    private final LocationDAO locationDAO = new LocationDAO();

    public List<Observation> getAll() throws SQLException {
        return dao.getAll();
    }

    public Observation getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Observation o) throws SQLException{
        validate(o);
        requireLocation(o.getLocationId());
        dao.add(o);
        LOG.info("Додано спостереження.");
    }

    public void update(Observation o) throws SQLException{
        validate(o);
        requireLocation(o.getLocationId());
        dao.update(o);
        LOG.info("Оновлено спостереження.");
    }

    public void delete(long id) throws SQLException{
        dao.delete(id);
        LOG.info("Видалено спостереження.");
    }

    static void validate(Observation o) {
        if(o==null)
            throw new IllegalArgumentException("Спостереження не може бути null.");
        if (o.getLocationId()<=0)
            throw new IllegalArgumentException("Некоректний locationId.");
        if(o.getIndicator()==null || o.getIndicator().isBlank())
            throw new IllegalArgumentException("Некоректна назва показника.");
        if(o.getObservedValue()<0 || o.getLimitValue()<=0)
            throw new IllegalArgumentException("Некоректні числові дані вимірювання.");
        if(o.getScaleScore()<0 || o.getScaleScore()>100 || o.getDurationScore()<0 || o.getDurationScore()>100)
            throw new IllegalArgumentException("F2 і F3 мають бути в діапазоні 0..100.");
    }
    private void requireLocation(long id) throws SQLException {
        if(locationDAO.getById(id)==null)
            throw new IllegalArgumentException("Локацію не знайдено."); }

}

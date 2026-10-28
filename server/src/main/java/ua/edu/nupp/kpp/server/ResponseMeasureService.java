package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ResponseMeasureService {
    private static final Logger LOG = Logger.getLogger(ResponseMeasure.class.getName());
    private final ResponseMeasureDAO dao = new ResponseMeasureDAO();
    private final IncidentDAO incidentDAO = new IncidentDAO();

    public List<ResponseMeasure> getAll() throws SQLException {
        return dao.getAll();
    }

    public void add(ResponseMeasure m) throws SQLException{
        validate(m);
        requireIncident(m.getIncidentId());
        dao.add(m);
        LOG.info("Додано захід реагування.");
    }

    public void update(ResponseMeasure m) throws SQLException{
        validate(m);
        requireIncident(m.getIncidentId());
        dao.update(m);
        LOG.info("Оновлено захід реагування.");
    }

    public void delete(long id) throws SQLException{
        dao.delete(id);
        LOG.info("Видалено захід реагування.");
    }

    static void validate(ResponseMeasure m) {
        if(m==null || m.getIncidentId()<=0 || m.getDescription()==null || m.getDescription().isBlank())
            throw new IllegalArgumentException("Некоректні дані для заходу реагування.");
    }
    private void requireIncident(long id) throws SQLException {
        if(incidentDAO.getById(id)==null)
            throw new IllegalArgumentException("Інцидент не знайдено."); }
}



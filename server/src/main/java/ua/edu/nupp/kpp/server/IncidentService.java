package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class IncidentService {
    private static final Logger LOG =
            Logger.getLogger(IncidentService.class.getName());

    private final IncidentDAO dao = new IncidentDAO();
    private final ObservationDAO observationDAO = new ObservationDAO();

    public List<Incident> getAll() throws SQLException {
        return dao.getAll();
    }

    public void add(long observationId) throws SQLException{
        dao.add(fromObservation(null, observationId, IncidentStatus.REGISTERED));
        LOG.info("Створено інцидент.");
    }

    public void update(long id, long observationId) throws SQLException{
        Incident old = dao.getById(id);
        if (old == null){
            throw new IllegalArgumentException("Інцидент не знайдено.");
        }
        LOG.info("Оновлено інцидент.");
    }

    public static boolean isAllowedTransition( IncidentStatus from, IncidentStatus to) {
        return (from == IncidentStatus.REGISTERED && to == IncidentStatus.VERIFIED)
                || (from == IncidentStatus.VERIFIED && to == IncidentStatus.RESPONSE)
                || (from == IncidentStatus.RESPONSE && to == IncidentStatus.CLOSED);
    }

        public void delete(long id) throws SQLException{
        dao.delete(id);
        LOG.info("Видалено інцидент.");
    }

    public void changeStatus(long id, IncidentStatus next) throws SQLException{
        Incident current = dao.getById(id);
        if (current == null){
            throw new IllegalArgumentException("Інцидент не знайдено.");
        }
        if (!isAllowedTransition(current.getStatus(), next)){
            LOG.warning("Недопустимий перехід стану: " + current.getStatus() + " -> " + next);
            throw new IllegalArgumentException("Перехід стану недоступний.");
        }
        dao.updateStatus(id, next);
        LOG.info("Змінено стан: "+ current.getStatus()+ " -> "+ next);
    }


    private Incident fromObservation(Long id, long observationId, IncidentStatus status)throws SQLException{
        Observation o = observationDAO.getById(observationId);
        if (o == null){
            throw new IllegalArgumentException("Спостереження не знайдено.");
        }
        double f1 = RiskCalculator.factor1(o.getObservedValue(), o.getLimitValue());
        double f2 = o.getScaleScore();
        double f3 = o.getDurationScore();
        double criticalIndex = RiskCalculator.calculate(f1, f2, f3);
        return new Incident(id, observationId, status, f1, f2, f3, criticalIndex);
    }



}

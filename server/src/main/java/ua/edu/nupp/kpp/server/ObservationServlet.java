package ua.edu.nupp.kpp.server;

import javax.servlet.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

public class ObservationServlet extends HttpServlet{
    private final ObservationService service = new ObservationService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            writeJson(resp, service.getAll());
        } catch (Exception e) {writeError(resp, e);}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String action=req.getParameter("action");
            if("delete".equals(action)) {
                service.delete(id(req));
            } else {
                Observation value = new Observation("update".equals(action) ? id(req) : null,
                        Long.parseLong(req.getParameter("locationId")),
                        req.getParameter("indicator"),
                        Double.parseDouble(req.getParameter("observedValue")),
                        Double.parseDouble(req.getParameter("limitValue")),
                        Double.parseDouble(req.getParameter("scaleScore")),
                        Double.parseDouble(req.getParameter("durationScore")),
                        req.getParameter("observedDate"));
                if ("add".equals(action)) service.add(value);
                else if ("update".equals(action)) service.update(value);
                else throw new IllegalArgumentException("Невідома операція.");
            }
            writeJson(resp, service.getAll());
        } catch(Exception e) { writeError(resp,e); }
    }

    private void writeJson(HttpServletResponse resp,Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(),data);
    }

    private void writeError(HttpServletResponse resp, Exception e) throws IOException {
        boolean clientError = e instanceof IllegalArgumentException;
        resp.setStatus(clientError
                ? HttpServletResponse.SC_BAD_REQUEST
                : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String message = clientError && e.getMessage() != null
                ? e.getMessage()
                : (clientError ? "Некоректний запит" : "Внутрішня помилка сервера");
        writeJson(resp, Map.of("error", message));
    }

    private long id(HttpServletRequest req) {
        return Long.parseLong(req.getParameter("id"));
    }


}

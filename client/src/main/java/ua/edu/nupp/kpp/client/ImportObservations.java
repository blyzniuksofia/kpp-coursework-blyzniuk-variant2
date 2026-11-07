package ua.edu.nupp.kpp.client;

import java.nio.file.*;
import java.util.List;
import java.util.logging.Logger;

public class ImportObservations {
        private static final Logger LOG=Logger.getLogger(ImportObservations.class.getName());
        private final Path file;
        private final HttpEcoIncidentClient api;
        public ImportObservations(Path file,HttpEcoIncidentClient api) { this.file=file; this.api=api; }

        public int importFirstRow() throws Exception {
            List<String> lines=Files.readAllLines(file);
            if(lines.size()<2) return 0;
            String[] p=lines.get(1).split(";");
            if(p.length!=7) { LOG.warning("Пропущено рядок 2"); return 0; }
            Observation o=new Observation(null,Long.parseLong(p[0]),p[1],Double.parseDouble(p[2]),
                    Double.parseDouble(p[3]),Double.parseDouble(p[4]),Double.parseDouble(p[5]),p[6]);
            api.addObservation(o);
            return 1;
        }

        public static void main(String[] args) throws Exception {
            int n=new ImportObservations(Path.of("sample-data","observations.csv"),new HttpEcoIncidentClient()).importFirstRow();
            System.out.println("Імпортовано: "+n);
        }
}

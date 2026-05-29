package cabinet.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private boolean firstWrite = true;

    private AuditService() {}

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String actiune) {
        // overwrite on first call each JVM run, append on subsequent calls
        try (PrintWriter pw = new PrintWriter(new FileWriter(AUDIT_FILE, !firstWrite))) {
            pw.println(actiune + "," + LocalDateTime.now().format(FMT));
            firstWrite = false;
        } catch (IOException e) {
            System.err.println("Eroare audit: " + e.getMessage());
        }
    }
}

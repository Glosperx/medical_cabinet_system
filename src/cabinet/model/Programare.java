package cabinet.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Programare implements Comparable<Programare> {

    public enum Status { PROGRAMATA, ANULATA, FINALIZATA }

    private Pacient pacient;
    private Medic medic;
    private LocalDateTime dateTime;
    private Status status;

    public Programare(Pacient pacient, Medic medic, LocalDateTime dateTime) {
        this.pacient = pacient;
        this.medic = medic;
        this.dateTime = dateTime;
        this.status = Status.PROGRAMATA;
    }

    public Pacient getPacient()        { return pacient; }
    public Medic getMedic()            { return medic; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Status getStatus()          { return status; }

    public void anuleaza()    { this.status = Status.ANULATA; }
    public void finalizeaza() { this.status = Status.FINALIZATA; }

    @Override
    public int compareTo(Programare other) {
        return this.dateTime.compareTo(other.dateTime);
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Programare{pacient=" + pacient.getName()
                + ", medic=" + medic.getName()
                + ", data=" + dateTime.format(fmt)
                + ", status=" + status + "}";
    }
}

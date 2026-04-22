package cabinet.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Programare implements Comparable<Programare> {

    public enum Status { PROGRAMATA, ANULATA, FINALIZATA }

    private Pacient pacient;
    private Medic medic;
    private LocalDateTime dataOra;
    private Status status;

    public Programare(Pacient pacient, Medic medic, LocalDateTime dataOra) {
        this.pacient = pacient;
        this.medic = medic;
        this.dataOra = dataOra;
        this.status = Status.PROGRAMATA;
    }

    public Pacient getPacient()        { return pacient; }
    public Medic getMedic()            { return medic; }
    public LocalDateTime getDataOra() { return dataOra; }
    public Status getStatus()          { return status; }

    public void anuleaza()    { this.status = Status.ANULATA; }
    public void finalizeaza() { this.status = Status.FINALIZATA; }

    @Override
    public int compareTo(Programare other) {
        return this.dataOra.compareTo(other.dataOra);
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Programare{pacient=" + pacient.getNume()
                + ", medic=" + medic.getNume()
                + ", data=" + dataOra.format(fmt)
                + ", status=" + status + "}";
    }
}

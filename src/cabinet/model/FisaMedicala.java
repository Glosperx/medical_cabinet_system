package cabinet.model;

import java.time.LocalDate;

public class FisaMedicala {

    private String diagnosis;
    private String treatment;
    private LocalDate date;

    public FisaMedicala(String diagnosis, String treatment, LocalDate date) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.date = date;
    }

    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public LocalDate getDate()   { return date; }

    @Override
    public String toString() {
        return "FisaMedicala{data=" + date
                + ", diagnostic=" + diagnosis
                + ", tratament=" + treatment + "}";
    }
}

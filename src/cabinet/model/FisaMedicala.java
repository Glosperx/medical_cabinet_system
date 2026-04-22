package cabinet.model;

import java.time.LocalDate;

public class FisaMedicala {

    private String diagnostic;
    private String tratament;
    private LocalDate data;

    public FisaMedicala(String diagnostic, String tratament, LocalDate data) {
        this.diagnostic = diagnostic;
        this.tratament = tratament;
        this.data = data;
    }

    public String getDiagnostic() { return diagnostic; }
    public String getTratament() { return tratament; }
    public LocalDate getData()   { return data; }

    @Override
    public String toString() {
        return "FisaMedicala{data=" + data
                + ", diagnostic=" + diagnostic
                + ", tratament=" + tratament + "}";
    }
}

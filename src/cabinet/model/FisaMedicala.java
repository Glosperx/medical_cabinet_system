package cabinet.model;

import java.time.LocalDate;

public class FisaMedicala {

    private long id = -1;
    private String pacientCnp;
    private String diagnostic;
    private String tratament;
    private LocalDate data;

    public FisaMedicala(String diagnostic, String tratament, LocalDate data) {
        this.diagnostic = diagnostic;
        this.tratament = tratament;
        this.data = data;
    }

    public long getId()            { return id; }
    public String getPacientCnp()  { return pacientCnp; }
    public String getDiagnostic()  { return diagnostic; }
    public String getTratament()   { return tratament; }
    public LocalDate getData()     { return data; }

    public void setId(long id)              { this.id = id; }
    public void setPacientCnp(String cnp)   { this.pacientCnp = cnp; }
    public void setTratament(String t)      { this.tratament = t; }

    @Override
    public String toString() {
        return "FisaMedicala{data=" + data
                + ", diagnostic=" + diagnostic
                + ", tratament=" + tratament + "}";
    }
}

package cabinet.model;

import java.util.ArrayList;
import java.util.List;

public class Pacient extends Persoana {

    private String grupaSangvina;
    private List<FisaMedicala> istoricMedical;

    public Pacient(String nume, String cnp, String telefon, String grupaSangvina) {
        super(nume, cnp, telefon);
        this.grupaSangvina = grupaSangvina;
        this.istoricMedical = new ArrayList<>();
    }

    public String getGrupaSangvina() { return grupaSangvina; }
    public List<FisaMedicala> getIstoricMedical() { return istoricMedical; }

    public void adaugaFisa(FisaMedicala fisa) {
        istoricMedical.add(fisa);
    }

    @Override
    public String toString() {
        return "Pacient{" + super.toString() + ", grupaSangvina=" + grupaSangvina + "}";
    }
}

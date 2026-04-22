package cabinet.service;

import cabinet.model.FisaMedicala;
import cabinet.model.Pacient;

import java.util.List;
import java.util.TreeSet;

public class PacientService {

    private TreeSet<Pacient> pacienti = new TreeSet<>();

    public void adaugaPacient(Pacient pacient) {
        pacienti.add(pacient);
    }

    public Pacient gasesteDupaCnp(String cnp) {
        return pacienti.stream()
                .filter(p -> p.getCnp().equals(cnp))
                .findFirst()
                .orElse(null);
    }

    public void adaugaFisaMedicala(String cnp, FisaMedicala fisa) {
        Pacient pacient = gasesteDupaCnp(cnp);
        if (pacient != null) {
            pacient.adaugaFisa(fisa);
        }
    }

    public List<FisaMedicala> getIstoricMedical(String cnp) {
        Pacient pacient = gasesteDupaCnp(cnp);
        if (pacient == null) return List.of();
        return pacient.getIstoricMedical();
    }

    public TreeSet<Pacient> getTotiPacientii() {
        return pacienti;
    }
}

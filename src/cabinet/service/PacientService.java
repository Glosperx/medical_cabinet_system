package cabinet.service;

import cabinet.model.FisaMedicala;
import cabinet.model.Pacient;

import java.util.List;
import java.util.TreeSet;

public class PacientService {

    private TreeSet<Pacient> pacienti = new TreeSet<>();

    public void addPatient(Pacient pacient) {
        pacienti.add(pacient);
    }

    public Pacient findByCnp(String cnp) {
        return pacienti.stream()
                .filter(p -> p.getCnp().equals(cnp))
                .findFirst()
                .orElse(null);
    }

    public void addMedicalRecord(String cnp, FisaMedicala fisa) {
        Pacient pacient = findByCnp(cnp);
        if (pacient != null) {
            pacient.addRecord(fisa);
        }
    }

    public List<FisaMedicala> getMedicalHistory(String cnp) {
        Pacient pacient = findByCnp(cnp);
        if (pacient == null) return List.of();
        return pacient.getMedicalHistory();
    }

    public TreeSet<Pacient> getAllPatients() {
        return pacienti;
    }
}

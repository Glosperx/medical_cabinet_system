package cabinet.service;

import cabinet.model.Medic;
import cabinet.model.Pacient;
import cabinet.model.Programare;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ProgramareService {

    private TreeMap<String, List<Programare>> programariPePacient = new TreeMap<>();

    public Programare programeaza(Pacient pacient, Medic medic, LocalDateTime dataOra) {
        Programare programare = new Programare(pacient, medic, dataOra);
        programariPePacient
                .computeIfAbsent(pacient.getCnp(), k -> new ArrayList<>())
                .add(programare);
        return programare;
    }

    public boolean anuleaza(Pacient pacient, LocalDateTime dataOra) {
        List<Programare> lista = programariPePacient.getOrDefault(pacient.getCnp(), List.of());
        for (Programare p : lista) {
            if (p.getDataOra().equals(dataOra) && p.getStatus() == Programare.Status.PROGRAMATA) {
                p.anuleaza();
                return true;
            }
        }
        return false;
    }

    public List<Programare> getProgramariPentruPacient(String cnp) {
        return programariPePacient.getOrDefault(cnp, List.of());
    }

    public List<Programare> getProgramariPentruMedic(Medic medic) {
        return programariPePacient.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getMedic().getCnp().equals(medic.getCnp()))
                .collect(Collectors.toList());
    }

    public List<Programare> getProgramariViitoare() {
        LocalDateTime now = LocalDateTime.now();
        return programariPePacient.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getStatus() == Programare.Status.PROGRAMATA
                        && p.getDataOra().isAfter(now))
                .sorted()
                .collect(Collectors.toList());
    }
}

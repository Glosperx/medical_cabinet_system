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

    private TreeMap<String, List<Programare>> programariPePatient = new TreeMap<>();

    public Programare schedule(Pacient pacient, Medic medic, LocalDateTime dateTime) {
        Programare programare = new Programare(pacient, medic, dateTime);
        programariPePatient
                .computeIfAbsent(pacient.getCnp(), k -> new ArrayList<>())
                .add(programare);
        return programare;
    }

    public boolean cancel(Pacient pacient, LocalDateTime dateTime) {
        List<Programare> lista = programariPePatient.getOrDefault(pacient.getCnp(), List.of());
        for (Programare p : lista) {
            if (p.getDateTime().equals(dateTime) && p.getStatus() == Programare.Status.PROGRAMATA) {
                p.anuleaza();
                return true;
            }
        }
        return false;
    }

    public List<Programare> getAppointmentsForPatient(String cnp) {
        return programariPePatient.getOrDefault(cnp, List.of());
    }

    public List<Programare> getAppointmentsForDoctor(Medic medic) {
        return programariPePatient.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getMedic().getCnp().equals(medic.getCnp()))
                .collect(Collectors.toList());
    }

    public List<Programare> getUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        return programariPePatient.values().stream()
                .flatMap(List::stream)
                .filter(p -> p.getStatus() == Programare.Status.PROGRAMATA
                        && p.getDateTime().isAfter(now))
                .sorted()
                .collect(Collectors.toList());
    }
}

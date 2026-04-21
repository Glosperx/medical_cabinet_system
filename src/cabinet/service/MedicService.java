package cabinet.service;

import cabinet.model.Medic;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MedicService {

    private TreeSet<Medic> medici = new TreeSet<>();

    public void addDoctor(Medic medic) {
        medici.add(medic);
    }

    public List<Medic> findBySpecialization(String specialization) {
        return medici.stream()
                .filter(m -> m.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }

    public TreeSet<Medic> getAllDoctors() {
        return medici;
    }
}

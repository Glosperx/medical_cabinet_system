package cabinet.service;

import cabinet.model.Medic;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MedicService {

    private TreeSet<Medic> medici = new TreeSet<>();

    public void adaugaMedic(Medic medic) {
        medici.add(medic);
    }

    public List<Medic> gasesteDupaSpecializare(String specializare) {
        return medici.stream()
                .filter(m -> m.getSpecializare().equalsIgnoreCase(specializare))
                .collect(Collectors.toList());
    }

    public TreeSet<Medic> getTotiMedicii() {
        return medici;
    }
}

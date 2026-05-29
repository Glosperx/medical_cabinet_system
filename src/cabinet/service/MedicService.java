package cabinet.service;

import cabinet.model.Medic;
import cabinet.repository.MedicRepository;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MedicService {

    private final MedicRepository medicRepo = MedicRepository.getInstance();

    public void adaugaMedic(Medic medic) {
        medicRepo.save(medic);
        AuditService.getInstance().log("adaugare_medic");
    }

    public List<Medic> gasesteDupaSpecializare(String specializare) {
        AuditService.getInstance().log("cautare_medic_specializare");
        return medicRepo.findAll().stream()
                .filter(m -> m.getSpecializare().equalsIgnoreCase(specializare))
                .collect(Collectors.toList());
    }

    public TreeSet<Medic> getTotiMedicii() {
        AuditService.getInstance().log("afisare_medici");
        return new TreeSet<>(medicRepo.findAll());
    }
}

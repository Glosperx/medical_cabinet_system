package cabinet.service;

import cabinet.model.FisaMedicala;
import cabinet.model.Pacient;
import cabinet.repository.FisaMedicalaRepository;
import cabinet.repository.PacientRepository;

import java.util.List;
import java.util.TreeSet;

public class PacientService {

    private final PacientRepository pacientRepo = PacientRepository.getInstance();
    private final FisaMedicalaRepository fisaRepo = FisaMedicalaRepository.getInstance();

    public void adaugaPacient(Pacient pacient) {
        pacientRepo.save(pacient);
        AuditService.getInstance().log("inregistrare_pacient");
    }

    public Pacient gasesteDupaCnp(String cnp) {
        return pacientRepo.findById(cnp).orElse(null);
    }

    public void adaugaFisaMedicala(String cnp, FisaMedicala fisa) {
        fisa.setPacientCnp(cnp);
        fisaRepo.save(fisa);
        AuditService.getInstance().log("adaugare_fisa_medicala");
    }

    public List<FisaMedicala> getIstoricMedical(String cnp) {
        AuditService.getInstance().log("vizualizare_istoric_medical");
        return fisaRepo.findByPacientCnp(cnp);
    }

    public TreeSet<Pacient> getTotiPacientii() {
        AuditService.getInstance().log("afisare_pacienti");
        return new TreeSet<>(pacientRepo.findAll());
    }
}

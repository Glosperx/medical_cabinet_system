package cabinet.service;

import cabinet.model.Medic;
import cabinet.model.Pacient;
import cabinet.model.Programare;
import cabinet.repository.ProgramareRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ProgramareService {

    private final ProgramareRepository programareRepo = ProgramareRepository.getInstance();

    public Programare programeaza(Pacient pacient, Medic medic, LocalDateTime dataOra) {
        Programare programare = new Programare(pacient, medic, dataOra);
        programareRepo.save(programare);
        AuditService.getInstance().log("programare_consultatie");
        return programare;
    }

    public boolean anuleaza(Pacient pacient, LocalDateTime dataOra) {
        AuditService.getInstance().log("anulare_programare");
        return programareRepo.anulareByPacientAndData(pacient.getCnp(), dataOra);
    }

    public List<Programare> getProgramariPentruPacient(String cnp) {
        AuditService.getInstance().log("filtrare_programari_pacient");
        return programareRepo.findByPacientCnp(cnp);
    }

    public List<Programare> getProgramariPentruMedic(Medic medic) {
        AuditService.getInstance().log("vizualizare_programari_medic");
        return programareRepo.findByMedicCnp(medic.getCnp());
    }

    public List<Programare> getProgramariViitoare() {
        AuditService.getInstance().log("vizualizare_programari_viitoare");
        return programareRepo.findViitoare();
    }
}

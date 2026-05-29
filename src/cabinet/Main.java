package cabinet;

import cabinet.db.DatabaseConnection;
import cabinet.model.*;
import cabinet.repository.*;
import cabinet.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Conectare la PostgreSQL si executarea schema.sql pentru a crea tabelele daca nu exista.
        // ── 1. Initializare schema BD ─────────────────────────────────────────
        System.out.println("=== Initializare baza de date ===");
        try {
            DatabaseConnection.getInstance().runScript("schema.sql");
            System.out.println("Schema initializata cu succes.\n");
        } catch (Exception e) {
            System.err.println("Eroare BD: " + e.getMessage());
            System.err.println("Asigurati-va ca PostgreSQL ruleaza si schema.sql exista.");
            return;
        }

        // Obtinere instante singleton ale repository-urilor si creare obiecte de servicii.
        // ── 2. Repository-uri si servicii ─────────────────────────────────────
        PacientRepository      pacientRepo = PacientRepository.getInstance();
        MedicRepository        medicRepo   = MedicRepository.getInstance();
        FisaMedicalaRepository fisaRepo    = FisaMedicalaRepository.getInstance();
        ProgramareRepository   progRepo    = ProgramareRepository.getInstance();

        PacientService    pacientService    = new PacientService();
        MedicService      medicService      = new MedicService();
        ProgramareService programareService = new ProgramareService();

        // Declararea obiectelor de demo folosite atat la seed cat si in apelurile de servicii.
        // ── 3. Definire date demo (o singura declarare) ───────────────────────
        MedicDeFamilie medicFamilie = new MedicDeFamilie(
                "Ana Ionescu", "1900101123456", "0721111111", "LIC-001", true);
        Specialist cardiolog = new Specialist(
                "Bogdan Popescu", "1850202234567", "0722222222", "LIC-002", "Cardiologie");
        Specialist neurolog = new Specialist(
                "Cristina Marin", "2900303345678", "0733333333", "LIC-003", "Neurologie");

        Pacient maria = new Pacient("Maria Dumitrescu", "2901010123456", "0744444444", "A+");
        Pacient ion   = new Pacient("Ion Vasilescu",    "1800202234567", "0755555555", "O-");

        LocalDateTime p1Data = LocalDateTime.of(2026, 12,  1,  9,  0);
        LocalDateTime p2Data = LocalDateTime.of(2026, 12, 10, 14, 30);
        LocalDateTime p3Data = LocalDateTime.of(2026, 12, 15, 11,  0);

        // Populare initiala a bazei de date; fiecare inregistrare este inserata doar daca nu exista deja.
        // ── 4. Seed: insereaza doar daca nu exista ────────────────────────────
        if (medicRepo.findById(medicFamilie.getCnp()).isEmpty()) medicService.adaugaMedic(medicFamilie);
        if (medicRepo.findById(cardiolog.getCnp()).isEmpty())    medicService.adaugaMedic(cardiolog);
        if (medicRepo.findById(neurolog.getCnp()).isEmpty())     medicService.adaugaMedic(neurolog);

        if (pacientRepo.findById(maria.getCnp()).isEmpty()) pacientService.adaugaPacient(maria);
        if (pacientRepo.findById(ion.getCnp()).isEmpty())   pacientService.adaugaPacient(ion);

        if (progRepo.findByPacientCnp(maria.getCnp()).stream().noneMatch(p -> p.getDataOra().equals(p1Data)))
            programareService.programeaza(maria, cardiolog, p1Data);
        if (progRepo.findByPacientCnp(maria.getCnp()).stream().noneMatch(p -> p.getDataOra().equals(p2Data)))
            programareService.programeaza(maria, medicFamilie, p2Data);
        if (progRepo.findByPacientCnp(ion.getCnp()).stream().noneMatch(p -> p.getDataOra().equals(p3Data)))
            programareService.programeaza(ion, neurolog, p3Data);

        if (fisaRepo.findByPacientCnp(maria.getCnp()).stream().noneMatch(f -> f.getDiagnostic().equals("Hipertensiune")))
            pacientService.adaugaFisaMedicala(maria.getCnp(),
                    new FisaMedicala("Hipertensiune", "Amlodipina 5mg", LocalDate.of(2026, 1, 15)));
        if (fisaRepo.findByPacientCnp(maria.getCnp()).stream().noneMatch(f -> f.getDiagnostic().equals("Anemie")))
            pacientService.adaugaFisaMedicala(maria.getCnp(),
                    new FisaMedicala("Anemie", "Suplimente cu fier", LocalDate.of(2026, 3, 20)));

        // Apelarea metodelor din servicii si afisarea datelor incarcate din baza de date.
        // ── 5. ETAPA I – Demo servicii (date incarcate din BD) ────────────────
        System.out.println("============================================================");
        System.out.println("  ETAPA I – Demonstratie servicii");
        System.out.println("============================================================");

        System.out.println("\n=== Toti medicii (sortati dupa nume) ===");
        medicService.getTotiMedicii().forEach(System.out::println);

        System.out.println("\n=== Toti pacientii (sortati dupa nume) ===");
        pacientService.getTotiPacientii().forEach(System.out::println);

        System.out.println("\n=== Programari viitoare (sortate dupa data) ===");
        programareService.getProgramariViitoare().forEach(System.out::println);

        // anulare idempotenta: SQL-ul verifica WHERE status='PROGRAMATA'
        boolean anulat = programareService.anuleaza(maria, p2Data);
        System.out.println("\n=== Anulare programare Maria pe 10 dec: "
                + (anulat ? "Succes" : "Deja anulata"));

        System.out.println("\n=== Programari pentru Maria Dumitrescu ===");
        programareService.getProgramariPentruPacient(maria.getCnp()).forEach(System.out::println);

        System.out.println("\n=== Programari pentru Dr. Bogdan Popescu ===");
        programareService.getProgramariPentruMedic(cardiolog).forEach(System.out::println);

        System.out.println("\n=== Istoric medical Maria Dumitrescu ===");
        pacientService.getIstoricMedical(maria.getCnp()).forEach(System.out::println);

        System.out.println("\n=== Cardiologi ===");
        medicService.gasesteDupaSpecializare("Cardiologie").forEach(System.out::println);

        Cabinet cabinet = new Cabinet("Clinica Medicala Centrala", "Str. Libertatii nr. 10");
        cabinet.adaugaMedic(medicFamilie);
        cabinet.adaugaMedic(cardiolog);
        System.out.println("\n=== Info cabinet ===");
        System.out.println(cabinet);
        cabinet.getMedici().forEach(d -> System.out.println("  -> " + d.getNume()
                + " [" + d.getRol() + ", " + d.getSpecializare() + "]"));

        // Demonstrarea operatiilor CRUD complete (save, findById, findAll, update, delete) pentru fiecare repository.
        // ── 6. ETAPA II – Demo CRUD prin Repository ───────────────────────────
        System.out.println("\n============================================================");
        System.out.println("  ETAPA II – Demonstratie CRUD prin Repository");
        System.out.println("============================================================");

        // CRUD pentru Pacient; cheia de identificare este CNP-ul (sir de caractere unic).
        // PacientRepository
        System.out.println("\n--- PacientRepository ---");
        Pacient testPacient = new Pacient("Test Pacient", "9990000000001", "0799000001", "B+");
        pacientRepo.save(testPacient);
        System.out.println("Save: " + testPacient);
        pacientRepo.findById("9990000000001").ifPresent(p -> System.out.println("FindById: " + p));
        System.out.println("FindAll (" + pacientRepo.findAll().size() + " pacienti):");
        pacientRepo.findAll().forEach(p -> System.out.println("  " + p));
        testPacient.setTelefon("0799000099");
        pacientRepo.update(testPacient);
        System.out.println("Update telefon: " + pacientRepo.findById("9990000000001").orElse(null));
        pacientRepo.delete("9990000000001");
        System.out.println("Delete: mai exista? " + pacientRepo.findById("9990000000001").isPresent());

        // CRUD pentru Medic; coloana discriminatoare tip_medic diferentiaza MedicDeFamilie de Specialist.
        // MedicRepository
        System.out.println("\n--- MedicRepository ---");
        Specialist testMedic = new Specialist("Test Specialist", "9990000000002", "0799000002", "LIC-T01", "Dermatologie");
        medicRepo.save(testMedic);
        System.out.println("Save: " + testMedic);
        medicRepo.findById("9990000000002").ifPresent(m -> System.out.println("FindById: " + m));
        System.out.println("FindAll (" + medicRepo.findAll().size() + " medici):");
        medicRepo.findAll().forEach(m -> System.out.println("  " + m));
        testMedic.setTelefon("0799000098");
        medicRepo.update(testMedic);
        System.out.println("Update telefon: " + medicRepo.findById("9990000000002").orElse(null));
        medicRepo.delete("9990000000002");
        System.out.println("Delete: mai exista? " + medicRepo.findById("9990000000002").isPresent());

        // CRUD pentru FisaMedicala; include si cautarea dupa CNP-ul pacientului.
        // FisaMedicalaRepository
        System.out.println("\n--- FisaMedicalaRepository ---");
        FisaMedicala testFisa = new FisaMedicala("Gastrita", "Omeprazol 20mg", LocalDate.of(2026, 4, 1));
        testFisa.setPacientCnp(maria.getCnp());
        fisaRepo.save(testFisa);
        System.out.println("Save (id=" + testFisa.getId() + "): " + testFisa);
        fisaRepo.findById(testFisa.getId()).ifPresent(f -> System.out.println("FindById: " + f));
        System.out.println("FindByPacientCnp pentru Maria:");
        fisaRepo.findByPacientCnp(maria.getCnp()).forEach(f -> System.out.println("  " + f));
        testFisa.setTratament("Omeprazol 40mg + dieta");
        fisaRepo.update(testFisa);
        System.out.println("Update tratament: " + fisaRepo.findById(testFisa.getId()).orElse(null));
        fisaRepo.delete(testFisa.getId());
        System.out.println("Delete: mai exista? " + fisaRepo.findById(testFisa.getId()).isPresent());

        // CRUD pentru Programare; citirea din BD reconstruieste obiectele Pacient si Medic prin JOIN.
        // ProgramareRepository
        System.out.println("\n--- ProgramareRepository ---");
        Programare testProg = new Programare(maria, neurolog, LocalDateTime.of(2026, 12, 1, 10, 0));
        progRepo.save(testProg);
        System.out.println("Save (id=" + testProg.getId() + "): " + testProg);
        progRepo.findById(testProg.getId()).ifPresent(p -> System.out.println("FindById: " + p));
        System.out.println("FindAll (" + progRepo.findAll().size() + " programari):");
        progRepo.findAll().forEach(p -> System.out.println("  " + p));
        testProg.finalizeaza();
        progRepo.update(testProg);
        System.out.println("Update status->FINALIZATA: " + progRepo.findById(testProg.getId()).orElse(null));
        progRepo.delete(testProg.getId());
        System.out.println("Delete: mai exista? " + progRepo.findById(testProg.getId()).isPresent());

        // Citire si afisare a fisierului audit.csv generat in timpul rularii curente.
        // ── 7. Audit ──────────────────────────────────────────────────────────
        System.out.println("\n============================================================");
        System.out.println("  AUDIT – Continut audit.csv");
        System.out.println("============================================================");
        try {
            List<String> linii = Files.readAllLines(Paths.get("audit.csv"));
            linii.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Nu s-a putut citi audit.csv: " + e.getMessage());
        }

        DatabaseConnection.getInstance().close();
    }
}

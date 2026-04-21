package cabinet;

import cabinet.model.*;
import cabinet.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        PacientService pacientService     = new PacientService();
        MedicService medicService         = new MedicService();
        ProgramareService programareService = new ProgramareService();

        // Adauga medici
        MedicDeFamilie medicFamilie = new MedicDeFamilie(
                "Ana Ionescu", "1900101123456", "0721111111", "LIC-001", true);
        Specialist cardiolog = new Specialist(
                "Bogdan Popescu", "1850202234567", "0722222222", "LIC-002", "Cardiologie");
        Specialist neurolog = new Specialist(
                "Cristina Marin", "2900303345678", "0733333333", "LIC-003", "Neurologie");

        medicService.addDoctor(medicFamilie);
        medicService.addDoctor(cardiolog);
        medicService.addDoctor(neurolog);

        System.out.println("=== Toti medicii (sortati dupa nume) ===");
        medicService.getAllDoctors().forEach(System.out::println);

        // Adauga pacienti
        Pacient maria = new Pacient("Maria Dumitrescu", "2901010123456", "0744444444", "A+");
        Pacient ion   = new Pacient("Ion Vasilescu",    "1800202234567", "0755555555", "O-");

        pacientService.addPatient(maria);
        pacientService.addPatient(ion);

        System.out.println("\n=== Toti pacientii (sortati dupa nume) ===");
        pacientService.getAllPatients().forEach(System.out::println);

        // Programeaza consultatii
        programareService.schedule(maria, cardiolog,    LocalDateTime.of(2026, 5, 10, 9, 0));
        programareService.schedule(maria, medicFamilie, LocalDateTime.of(2026, 5,  3, 14, 30));
        programareService.schedule(ion,   neurolog,     LocalDateTime.of(2026, 5,  8, 11, 0));

        System.out.println("\n=== Programari viitoare (sortate dupa data) ===");
        programareService.getUpcomingAppointments().forEach(System.out::println);

        // Anuleaza o programare
        boolean anulat = programareService.cancel(maria, LocalDateTime.of(2026, 5, 3, 14, 30));
        System.out.println("\n=== Anulare programare Maria pe 3 mai: "
                + (anulat ? "Succes" : "Esuat"));

        System.out.println("\n=== Programari pentru Maria Dumitrescu ===");
        programareService.getAppointmentsForPatient(maria.getCnp()).forEach(System.out::println);

        System.out.println("\n=== Programari pentru Dr. Bogdan Popescu ===");
        programareService.getAppointmentsForDoctor(cardiolog).forEach(System.out::println);

        // Adauga fise medicale
        pacientService.addMedicalRecord(maria.getCnp(),
                new FisaMedicala("Hipertensiune", "Amlodipina 5mg", LocalDate.of(2026, 1, 15)));
        pacientService.addMedicalRecord(maria.getCnp(),
                new FisaMedicala("Anemie", "Suplimente cu fier", LocalDate.of(2026, 3, 20)));

        System.out.println("\n=== Istoric medical Maria Dumitrescu ===");
        pacientService.getMedicalHistory(maria.getCnp()).forEach(System.out::println);

        // Cauta medici dupa specializare
        System.out.println("\n=== Cardiologi ===");
        medicService.findBySpecialization("Cardiologie").forEach(System.out::println);

        // Cabinet
        Cabinet cabinet = new Cabinet("Clinica Medicala Centrala", "Str. Libertatii nr. 10");
        cabinet.addDoctor(medicFamilie);
        cabinet.addDoctor(cardiolog);

        System.out.println("\n=== Info cabinet ===");
        System.out.println(cabinet);
        cabinet.getDoctors().forEach(d -> System.out.println("  -> " + d.getName()
                + " [" + d.getRole() + ", " + d.getSpecialization() + "]"));
    }
}

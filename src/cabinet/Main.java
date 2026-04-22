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

        medicService.adaugaMedic(medicFamilie);
        medicService.adaugaMedic(cardiolog);
        medicService.adaugaMedic(neurolog);

        System.out.println("=== Toti medicii (sortati dupa nume) ===");
        medicService.getTotiMedicii().forEach(System.out::println);

        // Adauga pacienti
        Pacient maria = new Pacient("Maria Dumitrescu", "2901010123456", "0744444444", "A+");
        Pacient ion   = new Pacient("Ion Vasilescu",    "1800202234567", "0755555555", "O-");

        pacientService.adaugaPacient(maria);
        pacientService.adaugaPacient(ion);

        System.out.println("\n=== Toti pacientii (sortati dupa nume) ===");
        pacientService.getTotiPacientii().forEach(System.out::println);

        // Programeaza consultatii
        programareService.programeaza(maria, cardiolog,    LocalDateTime.of(2026, 5, 10, 9, 0));
        programareService.programeaza(maria, medicFamilie, LocalDateTime.of(2026, 5,  3, 14, 30));
        programareService.programeaza(ion,   neurolog,     LocalDateTime.of(2026, 5,  8, 11, 0));

        System.out.println("\n=== Programari viitoare (sortate dupa data) ===");
        programareService.getProgramariViitoare().forEach(System.out::println);

        // Anuleaza o programare
        boolean anulat = programareService.anuleaza(maria, LocalDateTime.of(2026, 5, 3, 14, 30));
        System.out.println("\n=== Anulare programare Maria pe 3 mai: "
                + (anulat ? "Succes" : "Esuat"));

        System.out.println("\n=== Programari pentru Maria Dumitrescu ===");
        programareService.getProgramariPentruPacient(maria.getCnp()).forEach(System.out::println);

        System.out.println("\n=== Programari pentru Dr. Bogdan Popescu ===");
        programareService.getProgramariPentruMedic(cardiolog).forEach(System.out::println);

        // Adauga fise medicale
        pacientService.adaugaFisaMedicala(maria.getCnp(),
                new FisaMedicala("Hipertensiune", "Amlodipina 5mg", LocalDate.of(2026, 1, 15)));
        pacientService.adaugaFisaMedicala(maria.getCnp(),
                new FisaMedicala("Anemie", "Suplimente cu fier", LocalDate.of(2026, 3, 20)));

        System.out.println("\n=== Istoric medical Maria Dumitrescu ===");
        pacientService.getIstoricMedical(maria.getCnp()).forEach(System.out::println);

        // Cauta medici dupa specializare
        System.out.println("\n=== Cardiologi ===");
        medicService.gasesteDupaSpecializare("Cardiologie").forEach(System.out::println);

        // Cabinet
        Cabinet cabinet = new Cabinet("Clinica Medicala Centrala", "Str. Libertatii nr. 10");
        cabinet.adaugaMedic(medicFamilie);
        cabinet.adaugaMedic(cardiolog);

        System.out.println("\n=== Info cabinet ===");
        System.out.println(cabinet);
        cabinet.getMedici().forEach(d -> System.out.println("  -> " + d.getNume()
                + " [" + d.getRol() + ", " + d.getSpecializare() + "]"));
    }
}

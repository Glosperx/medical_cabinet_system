# Sistem de Gestiune Cabinet Medical - Etapa I

Sistemul este conceput pentru a gestiona activitatea dintr-un cabinet medical, incluzand managementul pacientilor, medicilor si programarilor.

## 1. Definirea sistemului

### Obiecte identificate (8 tipuri):
1. **Persoana** (Clasa abstracta de baza)
2. **Pacient** (Extinde Persoana)
3. **Medic** (Clasa abstracta, extinde Persoana)
4. **MedicDeFamilie** (Extinde Medic)
5. **Specialist** (Extinde Medic)
6. **Programare** (Gestiunea legaturii dintre pacient, medic si timp)
7. **FisaMedicala** (Istoricul medical al unui pacient)
8. **Cabinet** (Entitate care grupeaza medicii si locatia)

### Actiuni / Interogari (10+):
1. Adaugarea unui medic in sistem.
2. Afisarea listei complete de medici (sortata alfabetic).
3. Cautarea medicilor dupa specializare (ex: Cardiologie).
4. Inregistrarea unui pacient nou.
5. Afisarea listei de pacienti (sortata dupa nume).
6. Adaugarea unei fise medicale noi pentru un pacient existent.
7. Vizualizarea istoricului medical complet al unui pacient.
8. Programarea unei consultatii noi.
9. Anularea unei programari existente.
10. Vizualizarea tuturor programarilor viitoare (ordonate cronologic).
11. Filtrarea programarilor pentru un anumit pacient.
12. Vizualizarea programarilor pentru un anumit medic.

## 2. Detalii Implementare

Aplicatia respecta toate cerintele tehnice pentru Etapa I:
- **Clase simple**: Toate modelele au atribute `private` si metode de acces (getters/setters).
- **Colectii**:
    - `List` (ArrayList) pentru gestionarea istoricului medical si a listelor de medici.
    - `TreeMap` pentru stocarea programarilor (ordonate automat dupa cheie) si a pacientilor.
    - `TreeSet` / `Collections.sort` folosit pentru afisarea datelor sortate.
- **Mostenire**: Ierarhia `Persoana` -> `Pacient`/`Medic` si `Medic` -> `MedicDeFamilie`/`Specialist`.
- **Servicii**: Operatiile sunt expuse prin clasele `MedicService`, `PacientService` si `ProgramareService`.
- **Main**: Clasa `cabinet.Main` demonstreaza functionalitatea completa a sistemului prin apeluri catre servicii.

## Instructiuni de rulare
1. Compilare: `javac -d out -sourcepath src src/cabinet/Main.java`
2. Executie: `java -cp out cabinet.Main`

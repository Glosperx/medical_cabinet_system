# Sistem de Gestiune Cabinet Medical – Etapa I + II

Sistemul gestioneaza activitatea unui cabinet medical: pacienti, medici, programari si fise medicale, cu persistenta in PostgreSQL si audit automat.

---

## 1. Definirea sistemului

### Obiecte identificate (8 tipuri):
1. **Persoana** – clasa abstracta de baza
2. **Pacient** – extinde Persoana
3. **Medic** – clasa abstracta, extinde Persoana
4. **MedicDeFamilie** – extinde Medic
5. **Specialist** – extinde Medic
6. **Programare** – leaga pacient, medic si timp
7. **FisaMedicala** – istoricul medical al unui pacient
8. **Cabinet** – grupeaza medicii si locatia

### Actiuni / Interogari (12):
1. Adaugarea unui medic in sistem
2. Afisarea listei complete de medici (sortata alfabetic)
3. Cautarea medicilor dupa specializare
4. Inregistrarea unui pacient nou
5. Afisarea listei de pacienti (sortata dupa nume)
6. Adaugarea unei fise medicale noi pentru un pacient
7. Vizualizarea istoricului medical al unui pacient
8. Programarea unei consultatii noi
9. Anularea unei programari existente
10. Vizualizarea tuturor programarilor viitoare (cronologic)
11. Filtrarea programarilor pentru un pacient
12. Vizualizarea programarilor pentru un medic

---

## 2. Detalii Implementare – Etapa I

- **Clase simple**: atribute `private`, getteri/setteri.
- **Colectii**: `List` (ArrayList), `TreeMap`, `TreeSet`.
- **Mostenire**: `Persoana → Pacient/Medic → MedicDeFamilie/Specialist`.
- **Servicii**: `MedicService`, `PacientService`, `ProgramareService`.
- **Main**: demonstratie completa a functionalitatii.

---

## 3. Detalii Implementare – Etapa II

### 3.1 Persistenta JDBC + PostgreSQL

| Clasa                  | Descriere                                                  |
|------------------------|------------------------------------------------------------|
| `DatabaseConnection`   | Singleton; citeste `db.properties`; gestioneaza conexiunea |
| `Repository<T,ID>`     | Interfata generica: `save`, `findById`, `findAll`, `update`, `delete` |
| `PacientRepository`    | CRUD pentru tabela `pacienti` (ID = CNP)                   |
| `MedicRepository`      | CRUD pentru tabela `medici`; coloana discriminatoare `tip_medic` (`MEDIC_FAMILIE` / `SPECIALIST`) |
| `FisaMedicalaRepository` | CRUD pentru `fise_medicale`; metoda extra `findByPacientCnp` |
| `ProgramareRepository` | CRUD pentru `programari` cu JOIN pe pacienti/medici; metode extra `findViitoare`, `findByPacientCnp`, `findByMedicCnp`, `anulareByPacientAndData` |

**Schema BD** (`schema.sql`):
```
pacienti        – id, cnp (UNIQUE), nume, telefon, grupa_sangvina
medici          – id, cnp (UNIQUE), nume, telefon, numar_licenta, specializare, tip_medic, poate_elibera_bilet, domeniu
programari      – id, pacient_cnp (FK), medic_cnp (FK), data_ora, status
fise_medicale   – id, pacient_cnp (FK), diagnostic, tratament, data
```
Toate FK-urile au `ON DELETE CASCADE`.

### 3.2 Serviciul de Audit

`AuditService` (singleton) scrie in `audit.csv` (radacina proiectului) cate o linie per actiune:
```
<nume_actiune>,<timestamp>
```
Cele 12 actiuni auditate corespund metodelor din servicii.

---

## 4. Structura proiectului

```
programare_cabinet_medical/
├── lib/
│   └── postgresql-42.7.3.jar
├── src/
│   ├── resources/
│   │   └── db.properties
│   └── cabinet/
│       ├── Main.java
│       ├── db/
│       │   └── DatabaseConnection.java
│       ├── model/
│       │   ├── Persoana.java
│       │   ├── Pacient.java
│       │   ├── Medic.java
│       │   ├── MedicDeFamilie.java
│       │   ├── Specialist.java
│       │   ├── Programare.java
│       │   ├── FisaMedicala.java
│       │   └── Cabinet.java
│       ├── repository/
│       │   ├── Repository.java
│       │   ├── PacientRepository.java
│       │   ├── MedicRepository.java
│       │   ├── ProgramareRepository.java
│       │   └── FisaMedicalaRepository.java
│       └── service/
│           ├── AuditService.java
│           ├── MedicService.java
│           ├── PacientService.java
│           └── ProgramareService.java
├── out/                    (fisiere .class compilate)
├── schema.sql
├── audit.csv               (generat la rulare)
└── README.md
```

---

## 5. Configurare PostgreSQL

Datele de conexiune se afla in `src/resources/db.properties`:
```
db.url=jdbc:postgresql://localhost:5434/medical_cabinet
db.user=glosper
db.password=ciscosecpa55
```

Asigurati-va ca baza de date `medical_cabinet` exista si utilizatorul are drepturi de creare tabele.

---

## 6. Instructiuni de rulare

### Prerequisite
- Java 17+
- PostgreSQL 14+ rulând pe portul 5434
- Baza de date `medical_cabinet` creata

### Compilare
```bash
javac -d out -cp "lib/*" -sourcepath src src/cabinet/Main.java
```

### Executie
```bash
java -cp "out:lib/*:src/resources" cabinet.Main
```

> Pe Windows inlocuiti `:` cu `;` in `-cp`.

La prima rulare se creeaza automat tabelele (prin `schema.sql`).  
La rulari ulterioare, inregistrarile existente sunt sarite (`ON CONFLICT DO NOTHING`).  
Fisierul `audit.csv` este actualizat la fiecare rulare.

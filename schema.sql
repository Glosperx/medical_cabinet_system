CREATE TABLE IF NOT EXISTS pacienti (
    id            SERIAL PRIMARY KEY,
    cnp           VARCHAR(13)  UNIQUE NOT NULL,
    nume          VARCHAR(100) NOT NULL,
    telefon       VARCHAR(15),
    grupa_sangvina VARCHAR(5)
);

CREATE TABLE IF NOT EXISTS medici (
    id                   SERIAL PRIMARY KEY,
    cnp                  VARCHAR(13)  UNIQUE NOT NULL,
    nume                 VARCHAR(100) NOT NULL,
    telefon              VARCHAR(15),
    numar_licenta        VARCHAR(50),
    specializare         VARCHAR(100),
    tip_medic            VARCHAR(20)  NOT NULL,
    poate_elibera_bilet  BOOLEAN      DEFAULT FALSE,
    domeniu              VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS programari (
    id           SERIAL PRIMARY KEY,
    pacient_cnp  VARCHAR(13) NOT NULL REFERENCES pacienti(cnp) ON DELETE CASCADE,
    medic_cnp    VARCHAR(13) NOT NULL REFERENCES medici(cnp)   ON DELETE CASCADE,
    data_ora     TIMESTAMP   NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'PROGRAMATA'
);

CREATE TABLE IF NOT EXISTS fise_medicale (
    id           SERIAL PRIMARY KEY,
    pacient_cnp  VARCHAR(13)  NOT NULL REFERENCES pacienti(cnp) ON DELETE CASCADE,
    diagnostic   VARCHAR(500),
    tratament    VARCHAR(500),
    data         DATE         NOT NULL
);

-- Elimina duplicate lasand doar inregistrarea cu id-ul cel mai mic
DELETE FROM programari WHERE id NOT IN (
    SELECT MIN(id) FROM programari GROUP BY pacient_cnp, medic_cnp, data_ora
);
DELETE FROM fise_medicale WHERE id NOT IN (
    SELECT MIN(id) FROM fise_medicale GROUP BY pacient_cnp, diagnostic, data
);

-- Constrangeri unice pentru a preveni duplicate in viitor
CREATE UNIQUE INDEX IF NOT EXISTS uniq_programare ON programari(pacient_cnp, medic_cnp, data_ora);
CREATE UNIQUE INDEX IF NOT EXISTS uniq_fisa       ON fise_medicale(pacient_cnp, diagnostic, data);

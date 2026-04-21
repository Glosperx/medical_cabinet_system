package cabinet.model;

import java.util.ArrayList;
import java.util.List;

public class Pacient extends Persoana {

    private String bloodType;
    private List<FisaMedicala> medicalHistory;

    public Pacient(String name, String cnp, String phone, String bloodType) {
        super(name, cnp, phone);
        this.bloodType = bloodType;
        this.medicalHistory = new ArrayList<>();
    }

    public String getBloodType() { return bloodType; }
    public List<FisaMedicala> getMedicalHistory() { return medicalHistory; }

    public void addRecord(FisaMedicala fisa) {
        medicalHistory.add(fisa);
    }

    @Override
    public String toString() {
        return "Pacient{" + super.toString() + ", grupaSangvina=" + bloodType + "}";
    }
}

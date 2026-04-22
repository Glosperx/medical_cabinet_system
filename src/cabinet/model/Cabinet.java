package cabinet.model;

import java.util.ArrayList;
import java.util.List;

public class Cabinet {

    private String nume;
    private String adresa;
    private List<Medic> medici;

    public Cabinet(String nume, String adresa) {
        this.nume = nume;
        this.adresa = adresa;
        this.medici = new ArrayList<>();
    }

    public String getNume()       { return nume; }
    public String getAdresa()    { return adresa; }
    public List<Medic> getMedici(){ return medici; }

    public void adaugaMedic(Medic medic) { medici.add(medic); }

    @Override
    public String toString() {
        return "Cabinet{nume=" + nume
                + ", adresa=" + adresa
                + ", medici=" + medici.size() + "}";
    }
}

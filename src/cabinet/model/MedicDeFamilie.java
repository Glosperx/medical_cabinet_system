package cabinet.model;

public class MedicDeFamilie extends Medic {

    private boolean poateEliberaBiletTriimitere;

    public MedicDeFamilie(String nume, String cnp, String telefon,
                          String numarLicenta, boolean poateEliberaBiletTriimitere) {
        super(nume, cnp, telefon, numarLicenta, "Medicina de Familie");
        this.poateEliberaBiletTriimitere = poateEliberaBiletTriimitere;
    }

    public boolean poateEliberaBiletTriimitere() { return poateEliberaBiletTriimitere; }

    @Override
    public String getRol() { return "MedicDeFamilie"; }
}

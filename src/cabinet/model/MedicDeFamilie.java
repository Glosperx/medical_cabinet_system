package cabinet.model;

public class MedicDeFamilie extends Medic {

    private boolean poateEliberaBiletTriimitere;

    public MedicDeFamilie(String name, String cnp, String phone,
                          String licenseNumber, boolean poateEliberaBiletTriimitere) {
        super(name, cnp, phone, licenseNumber, "Medicina de Familie");
        this.poateEliberaBiletTriimitere = poateEliberaBiletTriimitere;
    }

    public boolean poateEliberaBiletTriimitere() { return poateEliberaBiletTriimitere; }

    @Override
    public String getRole() { return "MedicDeFamilie"; }
}

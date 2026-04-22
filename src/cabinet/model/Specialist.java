package cabinet.model;

public class Specialist extends Medic {

    private String domeniu;

    public Specialist(String nume, String cnp, String telefon,
                      String numarLicenta, String domeniu) {
        super(nume, cnp, telefon, numarLicenta, domeniu);
        this.domeniu = domeniu;
    }

    public String getDomeniu() { return domeniu; }

    @Override
    public String getRol() { return "Specialist"; }
}

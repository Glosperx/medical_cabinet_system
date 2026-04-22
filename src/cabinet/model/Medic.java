package cabinet.model;

public abstract class Medic extends Persoana {

    private String numarLicenta;
    protected String specializare;

    public Medic(String nume, String cnp, String telefon, String numarLicenta, String specializare) {
        super(nume, cnp, telefon);
        this.numarLicenta = numarLicenta;
        this.specializare = specializare;
    }

    public String getNumarLicenta()  { return numarLicenta; }
    public String getSpecializare() { return specializare; }

    public abstract String getRol();

    @Override
    public String toString() {
        return getRol() + "{" + super.toString() + ", specializare=" + specializare + "}";
    }
}

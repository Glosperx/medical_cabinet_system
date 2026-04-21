package cabinet.model;

public abstract class Medic extends Persoana {

    private String licenseNumber;
    protected String specialization;

    public Medic(String name, String cnp, String phone, String licenseNumber, String specialization) {
        super(name, cnp, phone);
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
    }

    public String getLicenseNumber()  { return licenseNumber; }
    public String getSpecialization() { return specialization; }

    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + "{" + super.toString() + ", specializare=" + specialization + "}";
    }
}

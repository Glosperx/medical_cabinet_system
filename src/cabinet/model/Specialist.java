package cabinet.model;

public class Specialist extends Medic {

    private String domain;

    public Specialist(String name, String cnp, String phone,
                      String licenseNumber, String domain) {
        super(name, cnp, phone, licenseNumber, domain);
        this.domain = domain;
    }

    public String getDomain() { return domain; }

    @Override
    public String getRole() { return "Specialist"; }
}

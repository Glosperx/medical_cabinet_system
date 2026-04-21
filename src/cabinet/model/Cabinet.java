package cabinet.model;

import java.util.ArrayList;
import java.util.List;

public class Cabinet {

    private String name;
    private String address;
    private List<Medic> medici;

    public Cabinet(String name, String address) {
        this.name = name;
        this.address = address;
        this.medici = new ArrayList<>();
    }

    public String getName()       { return name; }
    public String getAddress()    { return address; }
    public List<Medic> getDoctors(){ return medici; }

    public void addDoctor(Medic medic) { medici.add(medic); }

    @Override
    public String toString() {
        return "Cabinet{name=" + name
                + ", address=" + address
                + ", medici=" + medici.size() + "}";
    }
}

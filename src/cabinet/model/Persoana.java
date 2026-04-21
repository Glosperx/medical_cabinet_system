package cabinet.model;

public abstract class Persoana implements Comparable<Persoana> {

    private String name;
    private String cnp;
    private String phone;

    public Persoana(String name, String cnp, String phone) {
        this.name = name;
        this.cnp = cnp;
        this.phone = phone;
    }

    public String getName()  { return name; }
    public String getCnp()   { return cnp; }
    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public int compareTo(Persoana other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persoana)) return false;
        return cnp.equals(((Persoana) o).cnp);
    }

    @Override
    public int hashCode() { return cnp.hashCode(); }

    @Override
    public String toString() {
        return name + " (CNP: " + cnp + ", Telefon: " + phone + ")";
    }
}

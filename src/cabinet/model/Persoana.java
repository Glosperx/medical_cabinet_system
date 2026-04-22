package cabinet.model;

public abstract class Persoana implements Comparable<Persoana> {

    private String nume;
    private String cnp;
    private String telefon;

    public Persoana(String nume, String cnp, String telefon) {
        this.nume = nume;
        this.cnp = cnp;
        this.telefon = telefon;
    }

    public String getNume()  { return nume; }
    public String getCnp()   { return cnp; }
    public String getTelefon() { return telefon; }

    public void setTelefon(String telefon) { this.telefon = telefon; }

    @Override
    public int compareTo(Persoana other) {
        return this.nume.compareTo(other.nume);
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
        return nume + " (CNP: " + cnp + ", Telefon: " + telefon + ")";
    }
}

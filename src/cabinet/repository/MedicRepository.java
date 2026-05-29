package cabinet.repository;

import cabinet.db.DatabaseConnection;
import cabinet.model.Medic;
import cabinet.model.MedicDeFamilie;
import cabinet.model.Specialist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicRepository implements Repository<Medic, String> {

    private MedicRepository() {}

    private static class SingletonHolder {
        private static final MedicRepository INSTANCE = new MedicRepository();
    }

    public static MedicRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Medic m) {
        String sql = "INSERT INTO medici(cnp, nume, telefon, numar_licenta, specializare, tip_medic, poate_elibera_bilet, domeniu) " +
                     "VALUES (?,?,?,?,?,?,?,?) ON CONFLICT (cnp) DO NOTHING";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, m.getCnp());
            ps.setString(2, m.getNume());
            ps.setString(3, m.getTelefon());
            ps.setString(4, m.getNumarLicenta());
            ps.setString(5, m.getSpecializare());
            if (m instanceof MedicDeFamilie mdf) {
                ps.setString(6, "MEDIC_FAMILIE");
                ps.setBoolean(7, mdf.poateEliberaBiletTriimitere());
                ps.setNull(8, Types.VARCHAR);
            } else {
                Specialist sp = (Specialist) m;
                ps.setString(6, "SPECIALIST");
                ps.setNull(7, Types.BOOLEAN);
                ps.setString(8, sp.getDomeniu());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare save Medic", e);
        }
    }

    @Override
    public Optional<Medic> findById(String cnp) {
        String sql = "SELECT * FROM medici WHERE cnp = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById Medic", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Medic> findAll() {
        String sql = "SELECT * FROM medici ORDER BY nume";
        List<Medic> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll Medic", e);
        }
        return list;
    }

    @Override
    public void update(Medic m) {
        String sql = "UPDATE medici SET nume = ?, telefon = ?, numar_licenta = ?, specializare = ?, poate_elibera_bilet = ?, domeniu = ? WHERE cnp = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, m.getNume());
            ps.setString(2, m.getTelefon());
            ps.setString(3, m.getNumarLicenta());
            ps.setString(4, m.getSpecializare());
            if (m instanceof MedicDeFamilie mdf) {
                ps.setBoolean(5, mdf.poateEliberaBiletTriimitere());
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setNull(5, Types.BOOLEAN);
                ps.setString(6, ((Specialist) m).getDomeniu());
            }
            ps.setString(7, m.getCnp());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update Medic", e);
        }
    }

    @Override
    public void delete(String cnp) {
        String sql = "DELETE FROM medici WHERE cnp = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete Medic", e);
        }
    }

    private Medic mapRow(ResultSet rs) throws SQLException {
        String tip   = rs.getString("tip_medic");
        String cnp   = rs.getString("cnp");
        String nume  = rs.getString("nume");
        String tel   = rs.getString("telefon");
        String lic   = rs.getString("numar_licenta");
        if ("MEDIC_FAMILIE".equals(tip)) {
            return new MedicDeFamilie(nume, cnp, tel, lic, rs.getBoolean("poate_elibera_bilet"));
        } else {
            return new Specialist(nume, cnp, tel, lic, rs.getString("domeniu"));
        }
    }
}

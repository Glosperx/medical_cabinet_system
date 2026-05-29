package cabinet.repository;

import cabinet.db.DatabaseConnection;
import cabinet.model.Pacient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacientRepository implements Repository<Pacient, String> {

    private static PacientRepository instance;

    private PacientRepository() {}

    public static synchronized PacientRepository getInstance() {
        if (instance == null) {
            instance = new PacientRepository();
        }
        return instance;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Pacient p) {
        String sql = "INSERT INTO pacienti(cnp, nume, telefon, grupa_sangvina) VALUES (?,?,?,?) ON CONFLICT (cnp) DO NOTHING";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getCnp());
            ps.setString(2, p.getNume());
            ps.setString(3, p.getTelefon());
            ps.setString(4, p.getGrupaSangvina());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare save Pacient", e);
        }
    }

    @Override
    public Optional<Pacient> findById(String cnp) {
        String sql = "SELECT cnp, nume, telefon, grupa_sangvina FROM pacienti WHERE cnp = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById Pacient", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Pacient> findAll() {
        String sql = "SELECT cnp, nume, telefon, grupa_sangvina FROM pacienti ORDER BY nume";
        List<Pacient> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll Pacient", e);
        }
        return list;
    }

    @Override
    public void update(Pacient p) {
        String sql = "UPDATE pacienti SET nume = ?, telefon = ?, grupa_sangvina = ? WHERE cnp = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getNume());
            ps.setString(2, p.getTelefon());
            ps.setString(3, p.getGrupaSangvina());
            ps.setString(4, p.getCnp());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update Pacient", e);
        }
    }

    @Override
    public void delete(String cnp) {
        String sql = "DELETE FROM pacienti WHERE cnp = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete Pacient", e);
        }
    }

    private Pacient mapRow(ResultSet rs) throws SQLException {
        return new Pacient(
                rs.getString("nume"),
                rs.getString("cnp"),
                rs.getString("telefon"),
                rs.getString("grupa_sangvina")
        );
    }
}

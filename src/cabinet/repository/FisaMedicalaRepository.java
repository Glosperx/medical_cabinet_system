package cabinet.repository;

import cabinet.db.DatabaseConnection;
import cabinet.model.FisaMedicala;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FisaMedicalaRepository implements Repository<FisaMedicala, Long> {

    private FisaMedicalaRepository() {}

    private static class SingletonHolder {
        private static final FisaMedicalaRepository INSTANCE = new FisaMedicalaRepository();
    }

    public static FisaMedicalaRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(FisaMedicala f) {
        String sql = "INSERT INTO fise_medicale(pacient_cnp, diagnostic, tratament, data) VALUES (?,?,?,?) " +
                     "ON CONFLICT (pacient_cnp, diagnostic, data) DO NOTHING RETURNING id";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, f.getPacientCnp());
            ps.setString(2, f.getDiagnostic());
            ps.setString(3, f.getTratament());
            ps.setDate(4, Date.valueOf(f.getData()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) f.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare save FisaMedicala", e);
        }
    }

    @Override
    public Optional<FisaMedicala> findById(Long id) {
        String sql = "SELECT id, pacient_cnp, diagnostic, tratament, data FROM fise_medicale WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById FisaMedicala", e);
        }
        return Optional.empty();
    }

    @Override
    public List<FisaMedicala> findAll() {
        String sql = "SELECT id, pacient_cnp, diagnostic, tratament, data FROM fise_medicale ORDER BY data";
        List<FisaMedicala> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll FisaMedicala", e);
        }
        return list;
    }

    public List<FisaMedicala> findByPacientCnp(String cnp) {
        String sql = "SELECT id, pacient_cnp, diagnostic, tratament, data FROM fise_medicale WHERE pacient_cnp = ? ORDER BY data";
        List<FisaMedicala> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findByPacientCnp FisaMedicala", e);
        }
        return list;
    }

    @Override
    public void update(FisaMedicala f) {
        String sql = "UPDATE fise_medicale SET diagnostic = ?, tratament = ?, data = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, f.getDiagnostic());
            ps.setString(2, f.getTratament());
            ps.setDate(3, Date.valueOf(f.getData()));
            ps.setLong(4, f.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update FisaMedicala", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM fise_medicale WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete FisaMedicala", e);
        }
    }

    private FisaMedicala mapRow(ResultSet rs) throws SQLException {
        FisaMedicala f = new FisaMedicala(
                rs.getString("diagnostic"),
                rs.getString("tratament"),
                rs.getDate("data").toLocalDate()
        );
        f.setId(rs.getLong("id"));
        f.setPacientCnp(rs.getString("pacient_cnp"));
        return f;
    }
}

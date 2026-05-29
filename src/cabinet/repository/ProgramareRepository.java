package cabinet.repository;

import cabinet.db.DatabaseConnection;
import cabinet.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProgramareRepository implements Repository<Programare, Long> {

    private ProgramareRepository() {}

    private static class SingletonHolder {
        private static final ProgramareRepository INSTANCE = new ProgramareRepository();
    }

    public static ProgramareRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Connection conn() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private static final String SELECT_BASE =
            "SELECT pr.id, pr.data_ora, pr.status, " +
            "  pac.cnp AS pac_cnp, pac.nume AS pac_nume, pac.telefon AS pac_tel, pac.grupa_sangvina, " +
            "  m.cnp AS med_cnp, m.nume AS med_nume, m.telefon AS med_tel, m.numar_licenta, " +
            "  m.specializare, m.tip_medic, m.poate_elibera_bilet, m.domeniu " +
            "FROM programari pr " +
            "JOIN pacienti pac ON pr.pacient_cnp = pac.cnp " +
            "JOIN medici m ON pr.medic_cnp = m.cnp ";

    @Override
    public void save(Programare p) {
        String sql = "INSERT INTO programari(pacient_cnp, medic_cnp, data_ora, status) VALUES (?,?,?,?) " +
                     "ON CONFLICT (pacient_cnp, medic_cnp, data_ora) DO NOTHING RETURNING id";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, p.getPacient().getCnp());
            ps.setString(2, p.getMedic().getCnp());
            ps.setTimestamp(3, Timestamp.valueOf(p.getDataOra()));
            ps.setString(4, p.getStatus().name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) p.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare save Programare", e);
        }
    }

    @Override
    public Optional<Programare> findById(Long id) {
        String sql = SELECT_BASE + "WHERE pr.id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById Programare", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Programare> findAll() {
        String sql = SELECT_BASE + "ORDER BY pr.data_ora";
        List<Programare> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll Programare", e);
        }
        return list;
    }

    public List<Programare> findByPacientCnp(String cnp) {
        String sql = SELECT_BASE + "WHERE pr.pacient_cnp = ? ORDER BY pr.data_ora";
        List<Programare> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findByPacientCnp Programare", e);
        }
        return list;
    }

    public List<Programare> findByMedicCnp(String cnp) {
        String sql = SELECT_BASE + "WHERE pr.medic_cnp = ? ORDER BY pr.data_ora";
        List<Programare> list = new ArrayList<>();
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findByMedicCnp Programare", e);
        }
        return list;
    }

    public List<Programare> findViitoare() {
        String sql = SELECT_BASE + "WHERE pr.status = 'PROGRAMATA' AND pr.data_ora > NOW() ORDER BY pr.data_ora";
        List<Programare> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findViitoare Programare", e);
        }
        return list;
    }

    public boolean anulareByPacientAndData(String pacientCnp, LocalDateTime dataOra) {
        String sql = "UPDATE programari SET status = 'ANULATA' WHERE pacient_cnp = ? AND data_ora = ? AND status = 'PROGRAMATA'";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, pacientCnp);
            ps.setTimestamp(2, Timestamp.valueOf(dataOra));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare anulare Programare", e);
        }
    }

    @Override
    public void update(Programare p) {
        String sql = "UPDATE programari SET data_ora = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(p.getDataOra()));
            ps.setString(2, p.getStatus().name());
            ps.setLong(3, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update Programare", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM programari WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete Programare", e);
        }
    }

    private Programare mapRow(ResultSet rs) throws SQLException {
        Pacient pacient = new Pacient(
                rs.getString("pac_nume"), rs.getString("pac_cnp"),
                rs.getString("pac_tel"), rs.getString("grupa_sangvina")
        );
        Medic medic;
        if ("MEDIC_FAMILIE".equals(rs.getString("tip_medic"))) {
            medic = new MedicDeFamilie(
                    rs.getString("med_nume"), rs.getString("med_cnp"),
                    rs.getString("med_tel"), rs.getString("numar_licenta"),
                    rs.getBoolean("poate_elibera_bilet")
            );
        } else {
            medic = new Specialist(
                    rs.getString("med_nume"), rs.getString("med_cnp"),
                    rs.getString("med_tel"), rs.getString("numar_licenta"),
                    rs.getString("domeniu")
            );
        }
        Programare p = new Programare(pacient, medic, rs.getTimestamp("data_ora").toLocalDateTime());
        p.setId(rs.getLong("id"));
        String status = rs.getString("status");
        if ("ANULATA".equals(status))    p.anuleaza();
        if ("FINALIZATA".equals(status)) p.finalizeaza();
        return p;
    }
}

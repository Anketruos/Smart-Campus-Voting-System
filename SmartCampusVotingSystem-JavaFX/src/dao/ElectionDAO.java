package dao;

import model.Election;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectionDAO {

    public void insert(Election election) throws Exception {
        String sql = "INSERT INTO elections (title, description, start_date, end_date, active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, election.getTitle());
            ps.setString(2, election.getDescription());
            ps.setString(3, election.getStartDate().toString());
            ps.setString(4, election.getEndDate().toString());
            ps.setBoolean(5, election.isActive());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    election.setId(keys.getInt(1));
                }
            }
        }
    }

    public Election findById(int id) throws Exception {
        String sql = "SELECT * FROM elections WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Election> findAll() throws Exception {
        List<Election> list = new ArrayList<>();
        String sql = "SELECT * FROM elections ORDER BY active DESC, start_date DESC, title";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Election> findActive() throws Exception {
        List<Election> list = new ArrayList<>();
        String sql = "SELECT * FROM elections WHERE active = 1 AND start_date <= ? AND end_date >= ? ORDER BY start_date, title";
        String today = java.time.LocalDate.now().toString();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, today);
            ps.setString(2, today);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public void update(Election election) throws Exception {
        String sql = "UPDATE elections SET title=?, description=?, start_date=?, end_date=?, active=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, election.getTitle());
            ps.setString(2, election.getDescription());
            ps.setString(3, election.getStartDate().toString());
            ps.setString(4, election.getEndDate().toString());
            ps.setBoolean(5, election.isActive());
            ps.setInt(6, election.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM elections WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Election mapRow(ResultSet rs) throws SQLException {
        return new Election(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("description"),
            java.time.LocalDate.parse(rs.getString("start_date")),
            java.time.LocalDate.parse(rs.getString("end_date")),
            rs.getBoolean("active")
        );
    }
}

package dao;

import model.Election;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectionDAO {

    public void insert(Election election) throws Exception {
        String sql = "INSERT INTO elections (title, description, start_date, end_date, active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, election.getTitle());
            ps.setString(2, election.getDescription());
            ps.setDate(3, Date.valueOf(election.getStartDate()));
            ps.setDate(4, Date.valueOf(election.getEndDate()));
            ps.setBoolean(5, election.isActive());
            ps.executeUpdate();
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
        String sql = "SELECT * FROM elections";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Election> findActive() throws Exception {
        List<Election> list = new ArrayList<>();
        String sql = "SELECT * FROM elections WHERE active = TRUE";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
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
            ps.setDate(3, Date.valueOf(election.getStartDate()));
            ps.setDate(4, Date.valueOf(election.getEndDate()));
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
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
            rs.getBoolean("active")
        );
    }
}

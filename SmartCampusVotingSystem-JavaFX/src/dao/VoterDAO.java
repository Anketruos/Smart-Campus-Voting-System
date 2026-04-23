package dao;

import model.Voter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoterDAO {

    public void insert(Voter voter) throws Exception {
        String sql = "INSERT INTO voters (student_id, name, email, password_hash, has_voted) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, voter.getStudentId());
            ps.setString(2, voter.getName());
            ps.setString(3, voter.getEmail());
            ps.setString(4, voter.getPasswordHash());
            ps.setBoolean(5, voter.isHasVoted());
            ps.executeUpdate();
        }
    }

    public Voter findByStudentId(String studentId) throws Exception {
        String sql = "SELECT * FROM voters WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Voter findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM voters WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public void markVoted(int voterId) throws Exception {
        String sql = "UPDATE voters SET has_voted = TRUE WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, voterId);
            ps.executeUpdate();
        }
    }

    public List<Voter> findAll() throws Exception {
        List<Voter> list = new ArrayList<>();
        String sql = "SELECT * FROM voters";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Voter mapRow(ResultSet rs) throws SQLException {
        return new Voter(
            rs.getInt("id"),
            rs.getString("student_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getBoolean("has_voted")
        );
    }
}

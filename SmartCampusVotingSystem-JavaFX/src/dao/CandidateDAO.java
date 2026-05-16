package dao;

import model.Candidate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO {

    public void insert(Candidate candidate) throws Exception {
        String sql = "INSERT INTO candidates (election_id, name, position, bio) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, candidate.getElectionId());
            ps.setString(2, candidate.getName());
            ps.setString(3, candidate.getPosition());
            ps.setString(4, candidate.getBio());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    candidate.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<Candidate> findByElection(int electionId) throws Exception {
        List<Candidate> list = new ArrayList<>();
        String sql = "SELECT * FROM candidates WHERE election_id = ? ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Candidate findById(int id) throws Exception {
        String sql = "SELECT * FROM candidates WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM candidates WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int countByElection(int electionId) throws Exception {
        String sql = "SELECT COUNT(*) FROM candidates WHERE election_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Candidate mapRow(ResultSet rs) throws SQLException {
        return new Candidate(
            rs.getInt("id"),
            rs.getInt("election_id"),
            rs.getString("name"),
            rs.getString("position"),
            rs.getString("bio")
        );
    }
}

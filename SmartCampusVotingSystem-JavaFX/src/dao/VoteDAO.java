package dao;

import model.Vote;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteDAO {

    public void insert(Vote vote) throws Exception {
        String sql = "INSERT INTO votes (voter_id, election_id, candidate_id, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vote.getVoterId());
            ps.setInt(2, vote.getElectionId());
            ps.setInt(3, vote.getCandidateId());
            ps.setTimestamp(4, Timestamp.valueOf(vote.getTimestamp()));
            ps.executeUpdate();
        }
    }

    public boolean hasVoted(int voterId, int electionId) throws Exception {
        String sql = "SELECT COUNT(*) FROM votes WHERE voter_id = ? AND election_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, voterId);
            ps.setInt(2, electionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    // Returns candidateId -> voteCount for a given election
    public Map<Integer, Integer> getResultsByElection(int electionId) throws Exception {
        Map<Integer, Integer> results = new HashMap<>();
        String sql = "SELECT candidate_id, COUNT(*) as total FROM votes WHERE election_id = ? GROUP BY candidate_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.put(rs.getInt("candidate_id"), rs.getInt("total"));
            }
        }
        return results;
    }

    public List<Vote> findAll() throws Exception {
        List<Vote> list = new ArrayList<>();
        String sql = "SELECT * FROM votes";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Vote(
                    rs.getInt("id"),
                    rs.getInt("voter_id"),
                    rs.getInt("election_id"),
                    rs.getInt("candidate_id"),
                    rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        }
        return list;
    }
}

package dao;

import model.PreapprovedVoter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreapprovedVoterDAO {

    public void insert(PreapprovedVoter pv) throws Exception {
        String sql = "INSERT INTO preapproved_voters (student_id, email, registered) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pv.getStudentId());
            ps.setString(2, pv.getEmail());
            ps.setBoolean(3, pv.isRegistered());
            ps.executeUpdate();
        }
    }

    public boolean isPreapproved(String studentId, String email) throws Exception {
        String sql = "SELECT COUNT(*) FROM preapproved_voters WHERE student_id = ? AND email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    public void markRegistered(String studentId) throws Exception {
        String sql = "UPDATE preapproved_voters SET registered = TRUE WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.executeUpdate();
        }
    }

    public List<PreapprovedVoter> findAll() throws Exception {
        List<PreapprovedVoter> list = new ArrayList<>();
        String sql = "SELECT * FROM preapproved_voters";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PreapprovedVoter(
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("email"),
                    rs.getBoolean("registered")
                ));
            }
        }
        return list;
    }
}

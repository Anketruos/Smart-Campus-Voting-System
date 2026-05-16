package dao;

import model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public Admin findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM admins WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRow(resultSet);
            }
        }
        return null;
    }

    private Admin mapRow(ResultSet resultSet) throws SQLException {
        return new Admin(
            resultSet.getInt("id"),
            resultSet.getString("username"),
            resultSet.getString("password_hash")
        );
    }
}

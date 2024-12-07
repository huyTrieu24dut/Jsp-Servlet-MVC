package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	public int isValidUser(String username, String password) {
	    System.out.println("Username: " + username);
	    System.out.println("Password: " + password);

	    int userId = -1;
	    String query = "SELECT id, password FROM users WHERE username = ?";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, username);
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                String dbPassword = rs.getString("password");
	                if (password.equals(dbPassword)) {
	                    userId = rs.getInt("id");
	                    break;
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return userId;
	}

}

package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	public boolean isValidUser(String username, String password) {
		System.out.println(username);
		System.out.println(password);
	    boolean ktra = false;
	    String query = "SELECT password FROM users WHERE username = ?";
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");

	        try (Connection conn = DatabaseConnection.getInstance().getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            
	            stmt.setString(1, username);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    if (password.equals(rs.getString("password"))) {
	                        ktra = true;
	                        break;
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ktra;
	}

}

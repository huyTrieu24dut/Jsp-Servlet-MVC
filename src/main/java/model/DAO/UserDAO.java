package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	public boolean isValidUser(String username, String password) {
		boolean ktra = false;
		String query = "SELECT password FROM admin WHERE username = ?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			try (Connection conn = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
				stmt.setString(1, username);
				
				try (ResultSet rs = stmt.executeQuery(query)) {
					while (rs.next()) {
						if (password.equals(rs.getString(2))) {
							ktra = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return ktra;
	}
}

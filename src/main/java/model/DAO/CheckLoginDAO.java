package model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckLoginDAO {
	public boolean isValidUser(String username, String password) {
		
		boolean ktra = false;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/DULIEU", "root", "");
			Statement smt = conn.createStatement();
			String sql = "SELECT * FROM admin";
			ResultSet rs = smt.executeQuery(sql);
			while (rs.next()) {
				if ((username.equals(rs.getString(1)) && password.equals(rs.getString(2)))) {
					ktra = true;
					break;
				}
			}
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return ktra;
	}
}

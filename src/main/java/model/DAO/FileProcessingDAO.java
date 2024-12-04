package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FileProcessingDAO {
	
	public void addFileProcessing(int userId, String filename, String filePath) {
	    String query = "INSERT INTO file_processing (user_id, filename, file_path, status) VALUES (?, ?, ?, 'PENDING')";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, userId);
	        stmt.setString(2, filename);
	        stmt.setString(3, filePath);
	        stmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void updateFileProcessingStatus(String filePath, String status, String outputPath) {
	    String query = "UPDATE file_processing SET status = ?, output_path = ? WHERE file_path = ?";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, status);
	        stmt.setString(2, outputPath);
	        stmt.setString(3, filePath);
	        stmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}

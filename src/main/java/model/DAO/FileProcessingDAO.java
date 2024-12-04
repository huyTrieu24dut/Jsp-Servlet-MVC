package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.bean.FileProcessing;

public class FileProcessingDAO {
	
	public FileProcessing getFileById(int fileId) {
		String query = "SELECT file_path, output_path, status FROM file_processing WHERE id = ?";
		try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, fileId);
			try (ResultSet rs = stmt.executeQuery(query)) {
				FileProcessing fileProcessing = new FileProcessing();
				fileProcessing.setId(fileId);
				fileProcessing.setInputFile(rs.getString("file_path"));
				fileProcessing.setOutputFile(rs.getString("output_file"));
				fileProcessing.setStatus(rs.getString("status"));
				return fileProcessing;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void addFileProcessing(int userId, String filePath) {
	    String query = "INSERT INTO file_processing (user_id, file_path) VALUES (?, ?)";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, userId);
	        stmt.setString(2, filePath);
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

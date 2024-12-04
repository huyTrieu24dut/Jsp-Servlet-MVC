package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	
	public List<FileProcessing> getFilesByUserId(int userId) {
		String query = "SELECT file_path, output_path, status FROM file_processing "
                + "JOIN users ON file_processing.user_id = users.id "
                + "WHERE users.id = ?";
		List<FileProcessing> fileProcessingList = new ArrayList<>();
		
		try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, userId);
			try (ResultSet rs = stmt.executeQuery(query)) {
				FileProcessing fileProcessing = new FileProcessing();
				fileProcessing.setId(userId);
				fileProcessing.setInputFile(rs.getString("file_path"));
				fileProcessing.setOutputFile(rs.getString("output_file"));
				fileProcessing.setStatus(rs.getString("status"));
				
				fileProcessingList.add(fileProcessing);
				return fileProcessingList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int addFileProcessing(int userId, String filePath) {
	    String query = "INSERT INTO file_processing (user_id, file_path) VALUES (?, ?)";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setInt(1, userId);
	        stmt.setString(2, filePath);
	        stmt.executeUpdate();
	        
	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                return rs.getInt(1); // Lấy giá trị ID mới
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return -1;
	}
	
	public void updateFileProcessingStatus(int id, String status, String outputPath) {
	    String query = "UPDATE file_processing SET status = ?, output_path = ? WHERE id = ?";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, status);
	        stmt.setString(2, outputPath);
	        stmt.setInt(3, id);
	        stmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}

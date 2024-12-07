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
		String query = "SELECT file_path, output_path, status FROM file_processing WHERE id = ? LIMIT 1	";
		try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, fileId);
			try (ResultSet rs = stmt.executeQuery()) {
				FileProcessing fileProcessing = new FileProcessing();
				while (rs.next()) {
					fileProcessing.setId(fileId);
					fileProcessing.setInputFile(rs.getString("file_path"));
					fileProcessing.setOutputFile(rs.getString("output_path"));
					fileProcessing.setStatus(rs.getString("status"));
					
				}
				return fileProcessing;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<FileProcessing> getFilesByUserId(int userId) {
	    String query = "SELECT file_processing.id, name, file_path, output_path, status FROM file_processing "
	            + "JOIN users ON file_processing.user_id = users.id "
	            + "WHERE users.id = ?";
	    List<FileProcessing> fileProcessingList = new ArrayList<>();
	    
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, userId);
	        
	        try (ResultSet rs = stmt.executeQuery()) { 
	            while (rs.next()) { 
	                FileProcessing fileProcessing = new FileProcessing();
	                fileProcessing.setId(rs.getInt("id"));
	                fileProcessing.setName(rs.getString("name"));
	                fileProcessing.setInputFile(rs.getString("file_path"));
	                fileProcessing.setOutputFile(rs.getString("output_path"));
	                fileProcessing.setStatus(rs.getString("status"));
	                
	                fileProcessingList.add(fileProcessing);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return fileProcessingList;
	}

	
	public int addFileProcessing(int userId, String fileName, String filePath) {
	    String query = "INSERT INTO file_processing (user_id, name, file_path) VALUES (?, ?, ?)";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setInt(1, userId);
	        stmt.setString(2, fileName);
	        stmt.setString(3, filePath);
		    System.out.println("chua them");
	        stmt.executeUpdate();
	        
	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	            	System.out.println("da them");
	                return rs.getInt(1);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    System.out.println("them loi");
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

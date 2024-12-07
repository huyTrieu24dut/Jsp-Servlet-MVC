package model.BO;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.DAO.FileProcessingDAO;
import model.bean.FileProcessing;

public class FileProcessingBO {
	ExecutorService executorService = Executors.newSingleThreadExecutor();
	FileProcessingDAO fileProcessingDAO = new FileProcessingDAO();
	
	public void processFileAsync(int id, String filePath) {
		executorService.submit(() -> {
			try {
				fileProcessingDAO.updateFileProcessingStatus(id, "PROCESSING", null);
				String outputPath = filePath.replace(".pdf", ".doc").replace("inputfiles", "outputfiles");
	            convertPdfToDoc(filePath, outputPath);
				
				convertPdfToDoc(filePath, outputPath);
				Thread.sleep(10000);
				fileProcessingDAO.updateFileProcessingStatus(id, "DONE", outputPath);
			} catch (Exception e) {
				fileProcessingDAO.updateFileProcessingStatus(id, "FAILED", null);
				e.printStackTrace();
			}
		});
	}
	
	public String convertPdfToDoc(String filePath, String outputPath) throws Exception {
		com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(filePath);
		pdfDocument.save(outputPath, com.aspose.pdf.SaveFormat.Doc);
		pdfDocument.close();
		return outputPath;
	}

	public FileProcessing getFileById(int fileId) {
		FileProcessing foundFile = fileProcessingDAO.getFileById(fileId);
		return foundFile;
	}
	
	public List<FileProcessing> getFilesByUserId(int userId) {
		List<FileProcessing> foundFiles = fileProcessingDAO.getFilesByUserId(userId);
		return foundFiles;
	}
	
	public int addFileProcessing(int userId, String fileName, String file_path) {
		return fileProcessingDAO.addFileProcessing(userId, fileName, file_path);
	}
}

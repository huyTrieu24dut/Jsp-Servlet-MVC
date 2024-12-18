package model.BO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

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
	
//	public String convertPdfToDoc(String filePath, String outputPath) throws Exception {
//	    try (XWPFDocument wordDocument = new XWPFDocument();
//	         PDDocument pdfDocument = PDDocument.load(new File(filePath));
//	         FileOutputStream out = new FileOutputStream(outputPath)) {
//	    	PDFTextStripper pdfStripper = new PDFTextStripper();
//	    	String text = pdfStripper.getText(pdfDocument);
//	    	
//	    	wordDocument.createParagraph().createRun().setText(text);
//
//	        wordDocument.write(out);
//	        System.out.println("Đã chuyển đổi PDF sang Word thành công: " + outputPath);
//	    }
//	    return outputPath;
//	}

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
	
	public File uploadFile(Part filePart, String uploadDir) throws IOException {
        File uploadDirFile = new File(uploadDir);
        System.out.println(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
            System.out.println("Đã tạo thư mục ở đường dẫn" + uploadDir);
        }
        
        String fileName = filePart.getSubmittedFileName();
        
        //Tải file về
        File uploadedFile = new File(uploadDir, fileName);
        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(uploadedFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        return uploadedFile;
	}

	public boolean downloadFile(String filePath, HttpServletResponse response) throws IOException {
        File file = new File(filePath);

        if (file.exists()) {
            response.setContentType("application/octet-stream");
            String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
            return true;
        }
		return false;
	}
}

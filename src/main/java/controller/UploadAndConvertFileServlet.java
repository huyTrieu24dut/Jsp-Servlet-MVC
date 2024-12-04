package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.BO.FileProcessingBO;
import model.bean.FileProcessing;

/**
 * Servlet implementation class FileController
 */
@WebServlet("/UploadAndConvertFileServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 50,      // 50MB
    maxRequestSize = 1024 * 1024 * 100   // 100MB
)
public class UploadAndConvertFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public UploadAndConvertFileServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 // Lấy file từ request
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();

        // Đường dẫn tạm thời để lưu file upload
        String uploadDir = "D:/inputfiles";
        File uploadDirFile = new File(uploadDir);
        System.out.println(uploadDir + " :::: " + uploadDirFile.toString());
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            System.out.println("Đã tạo thư mục ở đường dẫn" + uploadDir);
        }
        FileProcessingBO fileProcessingBO = new FileProcessingBO();
        int fileProcessId = fileProcessingBO.addFileProcessing(1, uploadDir);
      
        File uploadedFile = new File(uploadDir, fileName);

        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(uploadedFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }

        // Xử lý file trong hàng đợi (giả lập)
        if (fileProcessId == -1) {
        	response.setContentType("text/plain");
            response.getWriter().write("Error when add process to database");
        } else {
        	fileProcessingBO.processFileAsync(fileProcessId, uploadedFile.getAbsolutePath());

            // Trả về phản hồi đơn giản
            response.setContentType("text/plain");
            response.getWriter().write("File uploaded and queued for processing");
        }
    }

}

package controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import model.BO.FileProcessingBO;

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
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        HttpSession session = request.getSession(false);
        Integer userId = (Integer)session.getAttribute("userId");

        String uploadDir = getServletContext().getRealPath("/inputfiles");
      
        FileProcessingBO fileProcessingBO = new FileProcessingBO();
        //up file len server
        File uploadedFile = fileProcessingBO.uploadFile(filePart, uploadDir);
        //Thêm thông tin vào db
        int fileProcessId = fileProcessingBO.addFileProcessing(userId, fileName, uploadDir);

        if (fileProcessId == -1) {
        	response.setContentType("text/plain");
            response.getWriter().write("Error when add process to database");
        } else {
        	fileProcessingBO.processFileAsync(fileProcessId, uploadedFile.getAbsolutePath());
            response.setContentType("text/plain");
            response.getWriter().write("File uploaded and queued for processing");
        }
    }

}

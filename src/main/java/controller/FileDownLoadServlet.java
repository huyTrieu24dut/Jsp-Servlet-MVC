package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BO.FileProcessingBO;

@WebServlet("/FileDownLoadServlet")
public class FileDownLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FileProcessingBO fileProcessingBO = new FileProcessingBO();
    public FileDownLoadServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int fileProcessId = Integer.parseInt(request.getParameter("fileId"));
		System.out.println("fileId = " + fileProcessId);
        
		String filePath = fileProcessingBO.getFileById(fileProcessId).getOutputFile();

        File file = new File(filePath);
        System.out.println(filePath);
        if (file.exists()) {
        	System.out.println("tìm thấy file");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        } else {
        	
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

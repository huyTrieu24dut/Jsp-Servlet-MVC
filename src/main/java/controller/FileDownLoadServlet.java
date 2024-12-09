package controller;

import java.io.IOException;

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
        System.out.println(filePath);

        boolean fileDownloaded = fileProcessingBO.downloadFile(filePath, response);

        if (!fileDownloaded) {
            System.out.println("File không tồn tại");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            System.out.println("File đã được tải thành công");
        }
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

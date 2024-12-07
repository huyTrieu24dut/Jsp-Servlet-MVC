package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.BO.FileProcessingBO;
import model.bean.FileProcessing;

@WebServlet("/FileProcessingListServlet")
public class FileProcessingListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FileProcessingBO fileProcessingBO;
	
    public FileProcessingListServlet() {
        super();
        this.fileProcessingBO = new FileProcessingBO();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Integer userId = (Integer)session.getAttribute("userId");
		
		System.out.println(userId);
		
		if (userId != null) {
			List<FileProcessing> fileProcessingList = fileProcessingBO.getFilesByUserId(userId);
			request.setAttribute("fileProcessingList", fileProcessingList);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/progress.jsp");
			rd.forward(request, response);
		} else {
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

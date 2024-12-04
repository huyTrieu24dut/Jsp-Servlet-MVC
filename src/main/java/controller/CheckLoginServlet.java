package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BO.CheckLoginBO;

@WebServlet("/CheckLoginServlet")
public class CheckLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public CheckLoginServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String destination = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println(username);
		System.out.println(password);
		CheckLoginBO checkLoginBO = new CheckLoginBO();
		try {
			if (checkLoginBO.isValidUser(username, password)) {
				destination = "/ConvertPage1.jsp";
				RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
				rd.forward(request, response);
			} else {
				destination = "/login.jsp";
				RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
				rd.forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package model.BO;

import model.DAO.UserDAO;

public class CheckLoginBO {
	UserDAO userDAO = new UserDAO();
	
	public boolean isValidUser(String username, String password) {
		return userDAO.isValidUser(username, password);
	}

}

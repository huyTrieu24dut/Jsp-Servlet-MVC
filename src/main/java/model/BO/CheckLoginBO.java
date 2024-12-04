package model.BO;

import java.util.ArrayList;

import model.DAO.UserDAO;
import model.bean.User;

public class CheckLoginBO {
	UserDAO checkLoginDAO = new UserDAO();
	
	public boolean isValidUser(String username, String password) {
		return checkLoginDAO.isValidUser(username, password);
	}

}

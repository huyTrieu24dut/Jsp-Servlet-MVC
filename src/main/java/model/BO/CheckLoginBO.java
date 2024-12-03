package model.BO;

import java.util.ArrayList;

import model.DAO.CheckLoginDAO;
import model.bean.User;

public class CheckLoginBO {
	CheckLoginDAO checkLoginDAO = new CheckLoginDAO();
	
	public boolean isValidUser(String username, String password) {
		return checkLoginDAO.isValidUser(username, password);
	}

}

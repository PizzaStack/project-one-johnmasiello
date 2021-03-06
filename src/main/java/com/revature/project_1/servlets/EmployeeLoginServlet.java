package com.revature.project_1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.project_1.dao.EmployeeDao;
import com.revature.project_1.model.LoginModel;

@WebServlet(urlPatterns="/login-employee",
		loadOnStartup=1)
public class EmployeeLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String LOGIN_ATTR = "loginToken";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if (username == null || password == null)  {
			logout(req, response);
			return;
		}
		LoginModel login = new EmployeeDao().authenticateLogin(username, password);
		
		if (login != null) {
			HttpSession session = req.getSession(true);
			session.setAttribute(LOGIN_ATTR, login);
			response.getWriter().write("success");
		} else {
			response.getWriter().write("failure");
		}
	}
	
	private void logout(HttpServletRequest req, HttpServletResponse response) throws IOException {
		HttpSession session = req.getSession(false);
		if (session != null)
			session.invalidate();
	}
}
